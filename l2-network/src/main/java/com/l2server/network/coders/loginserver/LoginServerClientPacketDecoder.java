package com.l2server.network.coders.loginserver;

import com.l2server.network.L2LoginClient;
import com.l2server.network.clientpackets.L2LoginClientPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

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
        /*log.debug("Decoding messaage: {}", Arrays.toString(byteBuf.array()));
        byteBuf.markReaderIndex();
        int dataSize = byteBuf.readShortLE() & 0xFFFF - HEADER_SIZE;
        L2LoginClient client = ctx.channel().attr(clientKey).get();
        final boolean descrypted = client.decrypt(byteBuf, dataSize);

        if (descrypted && byteBuf.readableBytes() > 0) {
            // apply limit
            final int limit = byteBuf.capacity();
            byteBuf.capacity(byteBuf.readerIndex() + dataSize);
            final L2LoginClientPacket cp = handlePacket(byteBuf);
            log.info("Packet got: {}", cp);

            if(cp != null) {
                list.add(cp);
            } else {
                byteBuf.resetReaderIndex();
            }

        }*/
    }

    private L2LoginClientPacket handlePacket(ByteBuf byteBuf) {
       /* int opcode = byteBuf.readByte() & 0xFF;

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
            packet._buf = byteBuf;
            if (packet.read()) {
                return packet;
            } else {
                return null;
            }
        }*/
        return null;
    }
}
