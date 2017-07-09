package com.l2server.network.util.crypt

import com.l2server.network.testutil.BuffersUtil.bufferData
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.testng.annotations.Test
import java.nio.ByteBuffer

/**
 * Phoen-X on 18.02.2017.
 */
class LoginCryptTest {

    @Test
    @Throws(Exception::class)
    fun shouldEncodeInitPacket() {
        val initPacketData = byteArrayOf(0, 0, 0, 97, 16, -41, -67, 33, -58, 0, 0, 115, 106, -56, -61, -114, 3, 9, 58, 119, 26, 77, 56, 106, -123, 59, -70, 86, -9, 119, 46, -100, 26, -89, -112, -23, 12, -1, 55, -93, -5, -21, 27, 107, -128, -92, -23, 52, -100, -32, -21, 80, -53, -32, 127, 14, 38, 23, -92, 67, -39, 113, 14, 34, 20, -1, -33, -89, -8, -20, 126, -59, -20, 108, -69, 126, -67, -59, -84, -77, -57, 92, 102, 105, -77, -120, -52, -48, 60, -46, -93, -28, -49, 47, 65, -101, 48, -21, 115, -51, 35, -91, -83, -87, 127, -74, 42, -88, 87, 32, 127, 74, -13, -99, 97, 41, -117, -103, -4, 70, 117, 107, -33, 74, 116, -88, 50, 109, 59, 120, -45, -86, -99, -128, -51, 90, 81, -118, -24, 78, -107, -35, 41, -4, -100, -61, 119, 32, -74, -83, -105, -9, -32, -67, 7, -117, 73, -87, -3, -62, -53, 102, 83, 58, 46, 18, 56, -100, -110, -71, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        val expectedEncoded = byteArrayOf(0, 0, 0, 97, 16, -41, -67, 33, -58, 0, 0, 115, 106, -56, -61, -114, 3, 9, 58, 119, 26, 77, 56, 106, -123, 59, -70, 86, -9, 119, 46, -100, 26, -89, -112, -23, 12, -1, 55, -93, -5, -21, 27, 107, -128, -92, -23, 52, -100, -32, -21, 80, -53, -32, 127, 14, 38, 23, -92, 67, -39, 113, 14, 34, 20, -1, -33, -89, -8, -20, 126, -59, -20, 108, -69, 126, -67, -59, -84, -77, -57, 92, 102, 105, -77, -120, -52, -48, 60, -46, -93, -28, -49, 47, 65, -101, 48, -21, 115, -51, 35, -91, -83, -87, 127, -74, 42, -88, 87, 32, 127, 74, -13, -99, 97, 41, -117, -103, -4, 70, 117, 107, -33, 74, 116, -88, 50, 109, 59, 120, -45, -86, -99, -128, -51, 90, 81, -118, -24, 78, -107, -35, 41, -4, -100, -61, 119, 32, -74, -83, -105, -9, -32, -67, 7, -117, 73, -87, -3, -62, -53, 102, 83, 58, 46, 18, 56, -100, -110, -71, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 30, 22, -102, 73, 113, -8, -41, 79, 64, -120, -110, 52, 49, 5, -42, 87, 70, -42, -95, -101, -128, -123, 71, 70, 70, -42, -95, -101, -128, -123, 71, 70, 70, -42, -95, -101, -128, -123, 71, 70, 70, -42, -95, -101, -128, -123, 71, 70, 70, -42, -95, -101, -128, -123, 71, 70, 70, -42, -95, -101, -128, -123, 71, 70, 70, -42, -95, -101, -128, -123, 71, 70, 70, -42, -95, -101, -128, -123, 71, 70, 70, -42, -95, -101, -128, -123, 71, 70, 70, -42, -95, -101, -128, -123, 71, 70, 70, -42, -95, -101, -128, -123, 71, 70, 70, -42, -95, -101, -128, -123, 71, 70, 70, -42, -95, -101, -128, -123, 71, 70, 70, -42, -95, -101, -128, -123, 71, 70, 70, -42, -95, -101, -128, -123, 71, 70, 70, -42, -95, -101, -128, -123, 71, 70, 70, -42, -95, -101, -128, -123, 71, 70, 70, -42, -95, -101, -128, -123, 71, 70, 70, -42, -95, -101, -128, -123, 71, 70, 70, -42, -95, -101, -128, -123, 71, 70, 70, -42, -95, -101, -128, -123, 71, 70, 70, -42, -95, -101, -128, -123, 71, 70, 70, -42, -95, -101, -128, -123, 71, 70, 0)

        val loginCrypt = LoginCrypt()
        loginCrypt.setKey(byteArrayOf(1, 2, 3))
        val buf = ByteBuffer.allocate(500)
        buf.put(initPacketData)
        val encodedBuffer = loginCrypt.encrypt(buf, initPacketData.size)
        val encodedData = bufferData(encodedBuffer)
        assertThat(encodedData).isEqualTo(expectedEncoded)
    }
}