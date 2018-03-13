package com.vvygulyarniy.l2.gameserver.network.packet.coder;

import com.l2server.network.communication.SessionId;
import com.vvygulyarniy.l2.gameserver.crypt.CryptService;
import com.vvygulyarniy.l2.gameserver.network.packet.server.L2GameServerPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

import static java.nio.ByteOrder.LITTLE_ENDIAN;

/**
 * Created by Phoen-X on 23.02.2017.
 */
@Slf4j
public class L2ServerPacketEncoder extends MessageToByteEncoder<L2GameServerPacket> {
    private static final AttributeKey<SessionId> sessionIdKey = AttributeKey.valueOf("gameSession");
    private final CryptService cryptService;

    public L2ServerPacketEncoder(CryptService cryptService) {
        this.cryptService = cryptService;
    }

    public static byte[] bufferData(ByteBuffer buffer) {
        int lastPos = buffer.position();
        buffer.position(0);
        byte[] data = new byte[lastPos];
        buffer.get(data);
        buffer.position(lastPos);
        return data;
    }


    @Override
    protected void encode(ChannelHandlerContext ctx, L2GameServerPacket msg, ByteBuf out) throws Exception {
        SessionId sessionId = ctx.channel().attr(sessionIdKey).get();

        ByteBuffer buffer = ByteBuffer.allocate(65000).order(LITTLE_ENDIAN);
        int headerPos = 0;

        int dataPos = headerPos + 2;
        buffer.position(dataPos);

        msg.write(buffer);

        int dataSize = buffer.position() - dataPos;
        buffer.position(dataPos);
        cryptService.encrypt(sessionId, buffer, dataSize);

        // recalculate size after encryption
        dataSize = buffer.position() - dataPos;

        buffer.position(headerPos);
        // write header
        buffer.putShort((short) (dataSize + 2));
        buffer.position(dataPos + dataSize);
        byte[] data = bufferData(buffer);

        out.writeBytes(data);
        log.info("Packet sent {}", msg);
    }
}
