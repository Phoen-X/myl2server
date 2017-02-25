package com.vvygulyarniy.l2.gameserver.network.netty;

import com.l2server.network.L2GameClient;
import com.l2server.network.clientpackets.game.L2GameClientPacket;
import com.vvygulyarniy.l2.gameserver.network.L2GameServerPacketProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameServerPacketHandler extends ChannelInboundHandlerAdapter {
    private static final AttributeKey<L2GameClient> gameClientKey = AttributeKey.valueOf("l2GameClient");
    private final L2GameServerPacketProcessor packetProcessor;

    public GameServerPacketHandler(L2GameServerPacketProcessor packetProcessor) {
        this.packetProcessor = packetProcessor;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("New connection acquired: {}", ctx);
        ctx.channel().attr(gameClientKey).set(new L2GameClient(null, ctx));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("Got msg: {}", msg);
        L2GameClient l2GameClient = ctx.channel().attr(gameClientKey).get();
        L2GameClientPacket packet = (L2GameClientPacket) msg;
        packet.process(packetProcessor, l2GameClient);
    }
}
