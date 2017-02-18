package com.l2server.network;

import java.nio.ByteBuffer;

/**
 * Created by Phoen-X on 16.02.2017.
 */
public interface BuffersHolder {
    ByteBuffer getPooledBuffer();

    void recycleBuffer(ByteBuffer primaryWriteBuffer);
}
