package com.vvygulyarniy.l2.loginserver;

import com.l2server.network.serverpackets.login.Init;
import com.l2server.network.util.crypt.LoginCrypt;
import org.testng.annotations.Test;

import java.nio.ByteBuffer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Phoen-X on 18.02.2017.
 */
public class SelectorThreadTest {
    @Test
    public void shouldEncodeInitPacket() throws Exception {
        Init init = new Init(new byte[]{1, 2, 3}, new byte[]{4, 5, 6}, 1);
        LoginCrypt crypt = new LoginCrypt();
        crypt.setKey(new byte[]{1, 2});
        ByteBuffer buffer = ByteBuffer.allocate(150);

        SelectorThread.putPacketIntoWriteBuffer(crypt, init, buffer);

        assertThat(buffer.position()).isEqualTo(50);
        assertThat(buffer.array()).isEqualTo(new byte[]{0, 50, -121, 26, -58, 52, 64, 62, -124, 65, 21, -119, -37, -43, 15, 117, 93, 60, 39, 72, -54, 29, -5, 63, 12, 119, -97, -28, -107, 104, -22, -94, 75, -50, 113, 33, 48, -46, 8, -34, 21, -100, 71, -128, -41, -15, -102, -70, 12, -63, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
    }


}