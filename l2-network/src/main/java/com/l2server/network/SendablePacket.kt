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
package com.l2server.network

import java.nio.ByteBuffer

abstract class SendablePacket {
    protected fun putInt(_buf: ByteBuffer, value: Int) {
        _buf.putInt(value)
    }

    protected fun putDouble(_buf: ByteBuffer, value: Double) {
        _buf.putDouble(value)
    }

    protected fun putFloat(_buf: ByteBuffer, value: Float) {
        _buf.putFloat(value)
    }

    /**
     * Write <B>byte</B> to the buffer. <BR></BR>
     * 8bit integer (00)

     * @param data
     */
    protected fun writeC(_buf: ByteBuffer, data: Int) {
        _buf.put(data.toByte())
    }


    /**
     * Write <B>double</B> to the buffer. <BR></BR>
     * 64bit double precision float (00 00 00 00 00 00 00 00)

     * @param value
     */
    protected fun writeF(_buf: ByteBuffer, value: Double) {
        _buf.putDouble(value)
    }

    /**
     * Write <B>short</B> to the buffer. <BR></BR>
     * 16bit integer (00 00)

     * @param value
     */
    protected fun writeH(_buf: ByteBuffer, value: Int) {
        _buf.putShort(value.toShort())
    }

    /**
     * Write <B>int</B> to the buffer. <BR></BR>
     * 32bit integer (00 00 00 00)

     * @param value
     */
    protected fun writeD(_buf: ByteBuffer, value: Int) {
        _buf.putInt(value)
    }

    /**
     * Write <B>long</B> to the buffer. <BR></BR>
     * 64bit integer (00 00 00 00 00 00 00 00)

     * @param value
     */
    protected fun writeQ(_buf: ByteBuffer, value: Long) {
        _buf.putLong(value)
    }

    /**
     * Write <B>byte[]</B> to the buffer. <BR></BR>
     * 8bit integer array (00 ...)

     * @param data
     */
    protected fun writeB(_buf: ByteBuffer, data: ByteArray) {
        _buf.put(data)
    }

    /**
     * Write <B>String</B> to the buffer.

     * @param _buf
     * *
     * @param text
     */
    protected fun writeS(_buf: ByteBuffer, text: String?) {
        if (text != null) {
            val len = text.length
            for (i in 0..len - 1) {
                _buf.putChar(text[i])
            }
        }

        _buf.putChar('\u0000')
    }

    abstract fun write(buffer: ByteBuffer)
}
