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

import java.nio.ByteBuffer;

/**
 * @author KenM
 */
public abstract class ReceivablePacket extends AbstractPacket {
    public NioNetStringBuffer _sbuf;

    protected ReceivablePacket() {

    }

    public abstract boolean read();

    /**
     * Reads <B>byte[]</B> from the buffer. <BR>
     * Reads as many bytes as the length of the array.
     *
     * @param dst : the byte array which will be filled with the data.
     */
    protected final void readB(final byte[] dst) {
        getBuffer().get(dst);
    }

    /**
     * Reads <B>byte[]</B> from the buffer. <BR>
     * Reads as many bytes as the given length (len). Starts to fill the
     * byte array from the given offset to <B>offset</B> + <B>len</B>.
     *
     * @param dst    : the byte array which will be filled with the data.
     * @param offset : starts to fill the byte array from the given offset.
     * @param len    : the given length of bytes to be read.
     */
    protected final void readB(final byte[] dst, final int offset, final int len) {
        getBuffer().get(dst, offset, len);
    }

    /**
     * Reads <B>byte</B> from the buffer. <BR>
     * 8bit integer (00)
     *
     * @return
     */
    protected final int readC() {
        return getBuffer().get() & 0xFF;
    }

    /**
     * Reads <B>short</B> from the buffer. <BR>
     * 16bit integer (00 00)
     *
     * @return
     */
    protected final int readH() {
        return getBuffer().getShort() & 0xFFFF;
    }

    /**
     * Reads <B>int</B> from the buffer. <BR>
     * 32bit integer (00 00 00 00)
     *
     * @return
     */
    protected final int readD() {
        return getBuffer().getInt();
    }

    /**
     * Reads <B>long</B> from the buffer. <BR>
     * 64bit integer (00 00 00 00 00 00 00 00)
     *
     * @return
     */
    protected final long readQ() {
        return getBuffer().getLong();
    }

    /**
     * Reads <B>double</B> from the buffer. <BR>
     * 64bit double precision float (00 00 00 00 00 00 00 00)
     *
     * @return
     */
    protected final double readF() {
        return getBuffer().getDouble();
    }

    /**
     * Reads <B>String</B> from the buffer.
     *
     * @return
     */
    protected final String readS() {
        _sbuf.clear();

        char ch;
        while ((ch = getBuffer().getChar()) != 0) {
            _sbuf.append(ch);
        }

        return _sbuf.toString();
    }

    /**
     * packet forge purpose
     *
     * @param data
     * @param sBuffer
     */
    public void setBuffers(ByteBuffer data, NioNetStringBuffer sBuffer) {
        setBuffer(data);
        _sbuf = sBuffer;
    }
}
