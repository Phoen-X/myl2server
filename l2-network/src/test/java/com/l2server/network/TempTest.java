package com.l2server.network;

import com.l2server.network.login.L2LoginClient;
import com.l2server.network.serverpackets.login.GGAuth;
import com.l2server.network.util.crypt.ScrambledKeyPair;
import org.testng.annotations.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.KeyPairGenerator;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.Arrays;

/**
 * Created by Phoen-X on 19.02.2017.
 */
public class TempTest {
    private byte[] putPacketIntoWriteBuffer(L2LoginClient client, GGAuth sp) {
        ByteBuffer WRITE_BUFFER = ByteBuffer.allocate(65000).order(ByteOrder.LITTLE_ENDIAN);
        WRITE_BUFFER.clear();
        int headerPos = WRITE_BUFFER.position();
        int dataPos = headerPos + 2;
        WRITE_BUFFER.position(dataPos);
        //sp.setClient(client);
        sp.write(WRITE_BUFFER);
        //sp.setBuf(null);
        int dataSize = WRITE_BUFFER.position() - dataPos;
        WRITE_BUFFER.position(dataPos);
        client.encrypt(WRITE_BUFFER, dataSize);
        dataSize = WRITE_BUFFER.position() - dataPos;
        WRITE_BUFFER.position(headerPos);
        WRITE_BUFFER.putShort((short) (dataSize + 2));
        int newPosition = dataPos + dataSize;
        WRITE_BUFFER.position(newPosition);

        return Arrays.copyOf(WRITE_BUFFER.array(), 35);
    }

    @Test
    public void testName() throws Exception {
        KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
        RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(1024, RSAKeyGenParameterSpec.F4);
        keygen.initialize(spec);
        ScrambledKeyPair keyPair = new ScrambledKeyPair(keygen.generateKeyPair());
        L2LoginClient client = new L2LoginClient(null, keyPair, new byte[]{1, 2});
        byte[] result = putPacketIntoWriteBuffer(client, new GGAuth(1));

        System.out.println(Arrays.toString(result));
    }
}
