package com.vvygulyarniy.l2.loginserver.netty

import com.l2server.network.clientpackets.login.L2LoginClientPacket
import com.l2server.network.login.L2LoginClient
import com.l2server.network.serverpackets.login.Init
import com.vvygulyarniy.l2.loginserver.LoginController
import com.vvygulyarniy.l2.loginserver.logic.LoginPacketsProcessor
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.util.AttributeKey
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Slf4j
class NettyLoginServerHandler(private val loginController: LoginController,
                              private val packetProcessor: LoginPacketsProcessor) : ChannelInboundHandlerAdapter() {
    private val clientKey = AttributeKey.valueOf<L2LoginClient>("l2LoginClient")

    @Throws(Exception::class)
    override fun channelRegistered(ctx: ChannelHandlerContext) {
        val client = L2LoginClient(NettyLoginClientConnection(ctx),
                loginController.scrambledRSAKeyPair!!,
                loginController.blowfishKey)
        ctx.channel().attr(clientKey).set(client)
        log.info("Channel registered: {}", ctx)
    }

    @Throws(Exception::class)
    override fun channelUnregistered(ctx: ChannelHandlerContext) {
        ctx.channel().attr(clientKey).set(null)
    }

    @Throws(Exception::class)
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        log.info("Got message: {}", msg)
        val client = ctx.channel().attr(clientKey).get()
        val packet = msg as L2LoginClientPacket
        packet.process(packetProcessor, client)
    }

    @Throws(Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext) {
        log.info("Channel active: {}", ctx)
        val client = ctx.channel().attr(clientKey).get()
        val initPacket = Init(client)


        ctx.channel().writeAndFlush(initPacket)
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(NettyLoginServerHandler::class.java)
    }

}
