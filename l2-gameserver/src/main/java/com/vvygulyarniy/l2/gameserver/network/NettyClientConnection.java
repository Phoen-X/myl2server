package com.vvygulyarniy.l2.gameserver.network;

import com.vvygulyarniy.l2.gameserver.network.packet.server.L2GameServerPacket;
import io.netty.channel.ChannelHandlerContext;

/**
 * Phoen-X on 12.03.2017.
 */
public class NettyClientConnection implements GameClientConnection {
    private final ChannelHandlerContext networkContext;

    public NettyClientConnection(ChannelHandlerContext networkContext) {
        this.networkContext = networkContext;
    }

    @Override
    public void sendPacket(L2GameServerPacket packet) {
        networkContext.writeAndFlush(packet);
    }
}
