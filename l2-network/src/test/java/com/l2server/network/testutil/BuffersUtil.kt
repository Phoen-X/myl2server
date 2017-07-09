package com.l2server.network.testutil

import java.nio.ByteBuffer

/**
 * Phoen-X on 19.02.2017.
 */
object BuffersUtil {

    fun bufferData(buffer: ByteBuffer): ByteArray {
        val lastPos = buffer.position()
        buffer.position(0)
        val data = ByteArray(lastPos + 1)
        buffer.get(data)
        buffer.position(lastPos)
        return data
    }
}
