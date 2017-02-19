package com.vvygulyarniy.l2.loginserver.netty;

import com.l2server.network.Init;
import com.l2server.network.L2LoginClient;
import com.l2server.network.clientpackets.L2LoginClientPacket;
import com.vvygulyarniy.l2.loginserver.LoginController;
import com.vvygulyarniy.l2.loginserver.logic.LoginPacketsProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyLoginServerHandler extends ChannelInboundHandlerAdapter {
    private final AttributeKey<L2LoginClient> clientKey = AttributeKey.valueOf("l2LoginClient");
    private final LoginController loginController;
    private final LoginPacketsProcessor packetProcessor;

    public NettyLoginServerHandler(LoginController loginController, LoginPacketsProcessor packetProcessor) {
        this.loginController = loginController;
        this.packetProcessor = packetProcessor;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        L2LoginClient client = new L2LoginClient(null, loginController.getScrambledRSAKeyPair(), loginController.getBlowfishKey());
        client.setPacketsSender(ctx::write);
        ctx.channel().attr(clientKey).set(client);
        log.info("CHannel registered: {}", ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().attr(clientKey).set(null);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("Got message: {}", msg);
        L2LoginClient client = ctx.channel().attr(clientKey).get();
        L2LoginClientPacket packet = (L2LoginClientPacket) msg;
        packet.process(packetProcessor, client);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("Channel active: {}", ctx);
        L2LoginClient client = ctx.channel().attr(clientKey).get();
        Init initPacket = new Init(client);


        ctx.channel().writeAndFlush(initPacket);
    }


}
