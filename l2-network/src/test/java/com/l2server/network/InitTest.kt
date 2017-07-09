package com.l2server.network

import com.l2server.network.serverpackets.login.Init
import com.l2server.network.testutil.BuffersUtil.bufferData
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.testng.annotations.Test
import java.nio.ByteBuffer

/**
 * Phoen-X on 18.02.2017.
 */
class InitTest {
    @Test
    fun shouldEncodeToBuffer() {
        val init = Init(byteArrayOf(1, 2, 3, 4), byteArrayOf(5, 6, 7, 8), 100500)
        val buffer = ByteBuffer.allocate(256)
        init.write(buffer)
        val data = bufferData(buffer)
        assertThat(data).isEqualTo(byteArrayOf(0, 0, 1, -120, -108, 0, 0, -58, 33, 1, 2, 3, 4, 41, -35, -107, 78, 119, -61, -100, -4, -105, -83, -74, 32, 7, -67, -32, -9, 5, 6, 7, 8, 0, 0))
    }
}