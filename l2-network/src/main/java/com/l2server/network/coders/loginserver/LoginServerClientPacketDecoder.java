package com.l2server.network.coders.loginserver;

import com.l2server.network.L2LoginClient;
import com.l2server.network.clientpackets.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

/**
 * Created by Phoen-X on 17.02.2017.
 */
@Slf4j
public class LoginServerClientPacketDecoder extends ByteToMessageDecoder {
    public static final int HEADER_SIZE = 2;
    private final AttributeKey<L2LoginClient> clientKey = AttributeKey.valueOf("l2LoginClient");

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        byteBuf.markReaderIndex();
        int dataSize = byteBuf.readShortLE() & 0xFFFF - HEADER_SIZE;
        L2LoginClient client = ctx.channel().attr(clientKey).get();
        ByteBuffer byteBuffer = ByteBuffer.allocate(byteBuf.readableBytes()).order(ByteOrder.LITTLE_ENDIAN);
        byte[] data = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(data);
        byteBuffer.put(data);
        byteBuffer.position(0);
        final boolean descrypted = client.decrypt(byteBuffer, dataSize);

        if (descrypted && byteBuffer.hasRemaining()) {
            // apply limit
            final int limit = byteBuffer.limit();
            byteBuf.capacity(byteBuffer.position() + dataSize);
            final L2LoginClientPacket cp = handlePacket(byteBuffer);
            log.info("Packet got: {}", cp);

            if(cp != null) {
                list.add(cp);
            } else {
                byteBuf.resetReaderIndex();
            }

        }
    }

    private L2LoginClientPacket handlePacket(ByteBuffer byteBuffer) {
        int opcode = byteBuffer.get() & 0xFF;

        L2LoginClientPacket packet = null;

        switch (opcode) {
            case 0x07:
                packet = new AuthGameGuard();
                break;
            case 0x00:
                packet = new RequestAuthLogin();
                break;
            case 0x02:
                packet = new RequestServerLogin();
                break;
            case 0x05:
                packet = new RequestServerList();
                break;
            default:
                break;
        }

        if (packet == null) {
            return null;
        } else {
            packet._buf = byteBuffer;
            if (packet.read()) {
                return packet;
            } else {
                return null;
            }
        }
    }
}
