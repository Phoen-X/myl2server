/* This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package com.l2server.network;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.WritableByteChannel;

/**
 * @param <T>
 * @author KenM
 */
@Slf4j
public class MMOConnection<T extends MMOClient<?>> {
    private final BuffersHolder buffersHolder;
    private final Socket _socket;
    private final InetAddress _address;
    private final ReadableByteChannel _readableByteChannel;
    private final WritableByteChannel _writableByteChannel;
    private final int _port;
    private final NioNetStackList<SendablePacket> _sendQueue;
    private final SelectionKey _selectionKey;
    private ByteBuffer _readBuffer;
    private ByteBuffer _primaryWriteBuffer;
    private ByteBuffer _secondaryWriteBuffer;
    private volatile boolean _pendingClose;
    private T _client;

    public MMOConnection(final BuffersHolder buffersHolder, final Socket socket, final SelectionKey key, boolean tcpNoDelay) {
        this.buffersHolder = buffersHolder;
        _socket = socket;
        _address = socket.getInetAddress();
        _readableByteChannel = socket.getChannel();
        _writableByteChannel = socket.getChannel();
        _port = socket.getPort();
        _selectionKey = key;

        _sendQueue = new NioNetStackList<>();

        try {
            _socket.setTcpNoDelay(tcpNoDelay);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public final T getClient() {
        return _client;
    }

    public final void setClient(final T client) {
        _client = client;
    }

    public final void sendPacket(final SendablePacket sp) {

        if (_pendingClose) {
            return;
        }

        synchronized (getSendQueue()) {
            _sendQueue.addLast(sp);
        }

        if (!_sendQueue.isEmpty()) {
            try {
                _selectionKey.interestOps(_selectionKey.interestOps() | SelectionKey.OP_WRITE);
            } catch (CancelledKeyException e) {
                // ignore
            }
        }
    }

    public final SelectionKey getSelectionKey() {
        return _selectionKey;
    }

    public final InetAddress getInetAddress() {
        return _address;
    }

    public final int getPort() {
        return _port;
    }

    public final void close() throws IOException {
        _socket.close();
    }

    public final int read(final ByteBuffer buf) throws IOException {
        return _readableByteChannel.read(buf);
    }

    public final int write(final ByteBuffer buf) throws IOException {
        return _writableByteChannel.write(buf);
    }

    public final void createWriteBuffer(final ByteBuffer buf) {
        if (_primaryWriteBuffer == null) {
            _primaryWriteBuffer = buffersHolder.getPooledBuffer();
            _primaryWriteBuffer.put(buf);
        } else {
            final ByteBuffer temp = buffersHolder.getPooledBuffer();
            temp.put(buf);

            final int remaining = temp.remaining();
            _primaryWriteBuffer.flip();
            final int limit = _primaryWriteBuffer.limit();

            if (remaining >= _primaryWriteBuffer.remaining()) {
                temp.put(_primaryWriteBuffer);
                buffersHolder.recycleBuffer(_primaryWriteBuffer);
                _primaryWriteBuffer = temp;
            } else {
                _primaryWriteBuffer.limit(remaining);
                temp.put(_primaryWriteBuffer);
                _primaryWriteBuffer.limit(limit);
                _primaryWriteBuffer.compact();
                _secondaryWriteBuffer = _primaryWriteBuffer;
                _primaryWriteBuffer = temp;
            }
        }
    }

    public final boolean hasPendingWriteBuffer() {
        return _primaryWriteBuffer != null;
    }

    public final void movePendingWriteBufferTo(final ByteBuffer dest) {
        _primaryWriteBuffer.flip();
        dest.put(_primaryWriteBuffer);
        buffersHolder.recycleBuffer(_primaryWriteBuffer);
        _primaryWriteBuffer = _secondaryWriteBuffer;
        _secondaryWriteBuffer = null;
    }

    public final ByteBuffer getReadBuffer() {
        return _readBuffer;
    }

    public final void setReadBuffer(final ByteBuffer buf) {
        _readBuffer = buf;
    }

    public final boolean isClosed() {
        return _pendingClose;
    }

    public final NioNetStackList<SendablePacket> getSendQueue() {
        return _sendQueue;
    }

    @SuppressWarnings("unchecked")
    public final void close(final SendablePacket sp) {

        close(new SendablePacket[]{sp});
    }

    public final void close(final SendablePacket[] closeList) {
        if (!this._pendingClose) {
            synchronized (this.getSendQueue()) {
                if (!this._pendingClose) {
                    this._pendingClose = true;
                    this._sendQueue.clear();
                    SendablePacket[] var3 = closeList;
                    int var4 = closeList.length;

                    for (int var5 = 0; var5 < var4; ++var5) {
                        SendablePacket sp = var3[var5];
                        this._sendQueue.addLast(sp);
                    }
                }
            }

            try {
                this._selectionKey.interestOps(this._selectionKey.interestOps() & -5);
            } catch (CancelledKeyException var8) {
                ;
            }

            try {
                this.close();
                log.info("Connection closed");
            } catch (IOException e) {
                log.error("Cannot close connection", e);
            }
        }
    }

    public final void releaseBuffers() {
        if (_primaryWriteBuffer != null) {
            buffersHolder.recycleBuffer(_primaryWriteBuffer);
            _primaryWriteBuffer = null;

            if (_secondaryWriteBuffer != null) {
                buffersHolder.recycleBuffer(_secondaryWriteBuffer);
                _secondaryWriteBuffer = null;
            }
        }

        if (_readBuffer != null) {
            buffersHolder.recycleBuffer(_readBuffer);
            _readBuffer = null;
        }
    }
}
