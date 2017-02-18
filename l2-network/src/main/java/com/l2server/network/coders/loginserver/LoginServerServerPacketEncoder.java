package com.l2server.network.coders.loginserver;

import com.l2server.network.L2LoginClient;
import com.l2server.network.L2LoginServerPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Phoen-X on 17.02.2017.
 */
@Slf4j
public class LoginServerServerPacketEncoder extends MessageToByteEncoder<L2LoginServerPacket> {

    /*@Override
    protected void encode(ChannelHandlerContext ctx, L2LoginServerPacket msg, ByteBuf out) throws Exception {
        log.debug("Encoding message {}", msg);
        out.markWriterIndex();
        msg._buf = out;
        msg.write();
        ByteBuf encrypted = Unpooled.copiedBuffer(out);
        encrypted.markReaderIndex();
        encrypted.readShort(); // skip the packet id
        getClient(ctx).encrypt(encrypted, encrypted.readableBytes());
        encrypted.resetReaderIndex();
        //ByteBuf encryptedData = packetData.readBytes(packetData.readableBytes());
        ByteBuf headerBuf = Unpooled.buffer(2);
        headerBuf.writeShort((short)(encrypted.readableBytes() + 2));
        out.resetWriterIndex();
        out.writeBytes(headerBuf);
        out.writeBytes(encrypted);
        out.writeByte(0);
        out.writeByte(0);
        out.markReaderIndex();
        byte[] bytes = new byte[out.readableBytes()];
        out.readBytes(bytes);
        out.resetReaderIndex();
        log.debug("Encoded message: {}", Arrays.toString(bytes));
    }*/

    private L2LoginClient getClient(ChannelHandlerContext ctx) {
        return ctx.channel().attr(AttributeKey.<L2LoginClient>valueOf("l2LoginClient")).get();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Got exception while encoding packet", cause);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, L2LoginServerPacket msg, ByteBuf out) throws Exception {

    }
}
