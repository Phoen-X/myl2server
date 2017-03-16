package com.vvygulyarniy.l2.gameserver.network;

import com.vvygulyarniy.l2.gameserver.network.packet.server.L2GameServerPacket;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * Phoen-X on 12.03.2017.
 */
@Slf4j
public class NettyClientConnection implements GameClientConnection {
    private final ChannelHandlerContext networkContext;

    public NettyClientConnection(ChannelHandlerContext networkContext) {
        this.networkContext = networkContext;
    }

    @Override
    public void sendPacket(L2GameServerPacket packet) {
        log.info("sending message {} to network handler {}", packet, networkContext);
        networkContext.writeAndFlush(packet);
    }
}
