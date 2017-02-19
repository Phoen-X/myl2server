package com.l2server.network.testutil;

import java.nio.ByteBuffer;

/**
 * Created by Phoen-X on 19.02.2017.
 */
public class BuffersUtil {

    public static byte[] bufferData(ByteBuffer buffer) {
        int lastPos = buffer.position();
        buffer.position(0);
        byte[] data = new byte[lastPos + 1];
        buffer.get(data);
        buffer.position(lastPos);
        return data;
    }
}
