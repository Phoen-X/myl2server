package com.vvygulyarniy.l2.gameserver.network.handler;

import com.vvygulyarniy.l2.gameserver.network.L2GameClient;
import com.vvygulyarniy.l2.gameserver.network.packet.L2ClientPacketProcessor;
import com.vvygulyarniy.l2.gameserver.network.packet.client.L2GameClientPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyHandler extends ChannelInboundHandlerAdapter {
    private static final AttributeKey<L2GameClient> gameClientKey = AttributeKey.valueOf("l2GameClient");
    private final L2ClientPacketProcessor packetProcessor;

    public NettyHandler(L2ClientPacketProcessor packetProcessor) {
        this.packetProcessor = packetProcessor;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("New connection acquired: {}", ctx);
        ctx.channel().attr(gameClientKey).set(new L2GameClient(ctx));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("Got msg: {}", msg);
        L2GameClient l2GameClient = ctx.channel().attr(gameClientKey).get();
        L2GameClientPacket packet = (L2GameClientPacket) msg;
        packet.process(packetProcessor, l2GameClient);
    }
}
