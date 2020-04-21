package com.l2server.network

import java.nio.ByteBuffer

abstract class ServerPacket {
    protected fun putInt(buffer: ByteBuffer, value: Int) {
        buffer.putInt(value)
    }

    protected fun putDouble(buffer: ByteBuffer, value: Double) {
        buffer.putDouble(value)
    }

    protected fun putFloat(buffer: ByteBuffer, value: Float) {
        buffer.putFloat(value)
    }

    /**
     * Write <B>byte</B> to the buffer. <BR></BR>
     * 8bit integer (00)

     * @param data
     */
    protected fun writeC(buffer: ByteBuffer, data: Int) {
        buffer.put(data.toByte())
    }


    /**
     * Write <B>double</B> to the buffer. <BR></BR>
     * 64bit double precision float (00 00 00 00 00 00 00 00)

     * @param value
     */
    protected fun writeF(buffer: ByteBuffer, value: Double) {
        buffer.putDouble(value)
    }

    /**
     * Write <B>short</B> to the buffer. <BR></BR>
     * 16bit integer (00 00)

     * @param value
     */
    protected fun writeH(buffer: ByteBuffer, value: Int) {
        buffer.putShort(value.toShort())
    }

    /**
     * Write <B>int</B> to the buffer. <BR></BR>
     * 32bit integer (00 00 00 00)

     * @param value
     */
    protected fun writeD(buffer: ByteBuffer, value: Int) {
        buffer.putInt(value)
    }

    /**
     * Write <B>long</B> to the buffer. <BR></BR>
     * 64bit integer (00 00 00 00 00 00 00 00)

     * @param value
     */
    protected fun writeQ(buffer: ByteBuffer, value: Long) {
        buffer.putLong(value)
    }

    /**
     * Write <B>byte[]</B> to the buffer. <BR></BR>
     * 8bit integer array (00 ...)

     * @param data
     */
    protected fun writeB(buffer: ByteBuffer, data: ByteArray) {
        buffer.put(data)
    }

    /**
     * Write <B>String</B> to the buffer.

     * @param buf
     * *
     * @param text
     */
    protected fun writeS(buf: ByteBuffer, text: String) {
        text.forEach { ch -> buf.putChar(ch) }
        buf.putChar('\u0000')
    }

    abstract fun write(buffer: ByteBuffer)
}
