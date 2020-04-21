package com.l2server.network

import java.nio.ByteBuffer
import kotlin.experimental.and

/**
 * @author KenM
 */
abstract class ClientPacket protected constructor(buffer: ByteBuffer) : AbstractPacket(buffer) {
    private var _sbuf: NioNetStringBuffer = NioNetStringBuffer(64 * 128)

    abstract fun read(): Boolean

    /**
     * Reads <B>byte[]</B> from the buffer. <BR></BR>
     * Reads as many bytes as the length of the array.
     *
     * @param dst : the byte array which will be filled with the data.
     */
    protected fun readB(dst: ByteArray) {
        buffer.get(dst)
    }

    /**
     * Reads <B>byte[]</B> from the buffer. <BR></BR>
     * Reads as many bytes as the given length (len). Starts to fill the
     * byte array from the given offset to <B>offset</B> + <B>len</B>.
     *
     * @param dst    : the byte array which will be filled with the data.
     * @param offset : starts to fill the byte array from the given offset.
     * @param len    : the given length of bytes to be read.
     */
    protected fun readB(dst: ByteArray, offset: Int, len: Int) {
        buffer.get(dst, offset, len)
    }

    /**
     * Reads <B>byte</B> from the buffer. <BR></BR>
     * 8bit integer (00)
     *
     * @return
     */
    protected fun readC(): Byte {
        return buffer.get() and 0xFF.toByte()
    }

    /**
     * Reads <B>short</B> from the buffer. <BR></BR>
     * 16bit integer (00 00)
     *
     * @return
     */
    protected fun readH(): Short {
        return buffer.short and 0xFFFF.toShort()
    }

    /**
     * Reads <B>int</B> from the buffer. <BR></BR>
     * 32bit integer (00 00 00 00)
     *
     * @return
     */
    protected fun readD(): Int {
        return buffer.int
    }

    /**
     * Reads <B>long</B> from the buffer. <BR></BR>
     * 64bit integer (00 00 00 00 00 00 00 00)
     *
     * @return
     */
    protected fun readQ(): Long {
        return buffer.long
    }

    /**
     * Reads <B>double</B> from the buffer. <BR></BR>
     * 64bit double precision float (00 00 00 00 00 00 00 00)
     *
     * @return
     */
    protected fun readF(): Double {
        return buffer.double
    }

    /**
     * Reads <B>String</B> from the buffer.
     *
     * @return
     */
    protected fun readS(): String {
        _sbuf.clear()

        var ch: Char
        ch = buffer.char
        while (ch.toInt() != 0) {
            _sbuf.append(ch)
            ch = buffer.char
        }

        return _sbuf.toString()
    }

    /**
     * packet forge purpose
     *
     * @param data
     * @param sBuffer
     */
    fun setBuffers(data: ByteBuffer, sBuffer: NioNetStringBuffer) {
        buffer = data
        _sbuf = sBuffer
    }
}
