package com.vvygulyarniy.l2.loginserver.netty;

import com.l2server.network.login.LoginClientConnection;
import com.l2server.network.serverpackets.login.L2LoginServerPacket;
import io.netty.channel.ChannelHandlerContext;

/**
 * Phoen-X on 04.07.2017.
 */
public class NettyLoginClientConnection implements LoginClientConnection {

    private final ChannelHandlerContext ctx;

    NettyLoginClientConnection(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void send(L2LoginServerPacket packet) {
        ctx.channel().writeAndFlush(packet);
    }

    @Override
    public void disconnect() {
        ctx.disconnect();
    }
}
