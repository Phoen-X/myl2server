package com.vvygulyarniy.l2.loginserver.netty;

import com.l2server.network.Init;
import com.l2server.network.L2LoginClient;
import com.vvygulyarniy.l2.loginserver.LoginController;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

import java.nio.ByteBuffer;

/**
 * Created by Phoen-X on 19.02.2017.
 */
public class NettyLoginServerHandler extends ChannelInboundHandlerAdapter {
    private final AttributeKey<L2LoginClient> clientKey = AttributeKey.valueOf("l2LoginClient");
    private final LoginController loginController;

    public NettyLoginServerHandler(LoginController loginController) {
        this.loginController = loginController;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        L2LoginClient client = new L2LoginClient(null, loginController.getScrambledRSAKeyPair(), loginController.getBlowfishKey());
        ctx.channel().attr(clientKey).set(client);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        L2LoginClient client = ctx.channel().attr(clientKey).get();
        Init initPacket = new Init(client);
        ByteBuffer buffer = ByteBuffer.allocate(65000);
        initPacket.write(buffer);
    }
}
