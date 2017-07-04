package com.l2server.network.coders.loginserver;

import com.l2server.network.login.L2LoginClient;
import com.l2server.network.serverpackets.login.L2LoginServerPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

import static java.nio.ByteOrder.LITTLE_ENDIAN;

/**
 * Created by Phoen-X on 17.02.2017.
 */
@Slf4j
public class LoginServerServerPacketEncoder extends MessageToByteEncoder<L2LoginServerPacket> {

    public static byte[] bufferData(ByteBuffer buffer) {
        int lastPos = buffer.position();
        buffer.position(0);
        byte[] data = new byte[lastPos];
        buffer.get(data);
        buffer.position(lastPos);
        return data;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, L2LoginServerPacket packet, ByteBuf out) throws Exception {
        log.info("Encoding message: {}", packet);
        L2LoginClient client = getClient(ctx);

        ByteBuffer buffer = ByteBuffer.allocate(65000).order(LITTLE_ENDIAN);
        int headerPos = 0;

        int dataPos = headerPos + 2;
        buffer.position(dataPos);

        packet.write(buffer);

        int dataSize = buffer.position() - dataPos;
        buffer.position(dataPos);
        client.getLoginCrypt().encrypt(buffer, dataSize);

        // recalculate size after encryption
        dataSize = buffer.position() - dataPos;

        buffer.position(headerPos);
        // write header
        buffer.putShort((short) (dataSize + 2));
        buffer.position(dataPos + dataSize);
        byte[] data = bufferData(buffer);

        out.writeBytes(data);
        log.info("Packet sent {}", packet);
    }

    private L2LoginClient getClient(ChannelHandlerContext ctx) {
        return ctx.channel().attr(AttributeKey.<L2LoginClient>valueOf("l2LoginClient")).get();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Got exception while encoding packet", cause);
    }
}
