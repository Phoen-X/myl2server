package com.l2server.network.coders.gameserver;

import com.l2server.network.L2GameClient;
import com.l2server.network.NioNetStringBuffer;
import com.l2server.network.clientpackets.game.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Phoen-X on 23.02.2017.
 */
@Slf4j
public class GameServerClientPacketDecoder extends ByteToMessageDecoder {
    private static final AttributeKey<L2GameClient> gameClientKey = AttributeKey.valueOf("l2GameClient");

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        L2GameClient client = ctx.channel().attr(gameClientKey).get();
        in.markReaderIndex();
        int dataSize = in.readShortLE() & 0xFFFF - 2;
        ByteBuffer byteBuffer = ByteBuffer.allocate(in.readableBytes() + 256).order(ByteOrder.LITTLE_ENDIAN);
        byte[] data = new byte[in.readableBytes()];
        in.readBytes(data);
        byteBuffer.put(data);
        byteBuffer.position(0);
        log.debug("Decoding packet: {}", Arrays.toString(data));
        final boolean ret = client.decrypt(byteBuffer, data.length);
        log.debug("Decoded packet: {}", Arrays.toString(byteBuffer.array()));
        if (ret && byteBuffer.hasRemaining()) {
            // apply limit
            final int limit = byteBuffer.limit();
            byteBuffer.limit(byteBuffer.position() + dataSize);
            final L2GameClientPacket cp = createPacket(byteBuffer);
            if (cp != null) {
                cp._buf = byteBuffer;
                cp._sbuf = new NioNetStringBuffer(64 * 1024);
                if (cp.read()) {
                    out.add(cp);
                } else {
                    in.resetReaderIndex();
                }
            } else {
                log.warn("Unknown packet, continuing");
            }
        }
    }

    public L2GameClientPacket createPacket(ByteBuffer buf) {
        int opCode = buf.get() & 0xFF;

        switch (opCode) {
            case 0x0e:
                return new ProtocolVersion();
            case 0x2b:
                return new AuthLogin();
            case 0x13:
                return new NewCharacter();
            case 0x0c:
                return new CharacterCreate();
            case 0x12:
                return new RequestPledgeSetAcademyMaster();
            case 0xd0:
                int subOpCode = -1;
                if (buf.remaining() >= 2) {
                    subOpCode = buf.getShort() & 0xffff;
                } else {
                    log.warn("Client sent a 0xd0 without the second opCode.");
                    return null;
                }

                switch (subOpCode) {
                    case 0x36:
                        return new RequestGotoLobby();
                    /*case 0x93:
                        return new RequestEx2ndPasswordCheck();
                        break;
                    case 0x94:
                        return new RequestEx2ndPasswordVerify();
                        break;
                    case 0x95:
                        return new RequestEx2ndPasswordReq();
                        break;*/
                    default:
                        log.warn("Unknown second opCode {} for opCode {}", Integer.toHexString(subOpCode), Integer.toHexString(opCode));
                        return null;
                }
            default:
                log.warn("Unknown packet opCode: {}", Integer.toHexString(opCode));
                return null;
        }
    }
}
