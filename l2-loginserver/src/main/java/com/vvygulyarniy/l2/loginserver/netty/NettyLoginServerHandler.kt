package com.vvygulyarniy.l2.loginserver.netty

import com.l2server.crypt.LoginCrypt
import com.vvygulyarniy.l2.loginserver.LoginController
import com.vvygulyarniy.l2.loginserver.logic.LoginPacketsProcessor
import com.vvygulyarniy.l2.loginserver.model.data.SessionId
import com.vvygulyarniy.l2.loginserver.netty.login.L2LoginClient
import com.vvygulyarniy.l2.loginserver.netty.packet.client.L2LoginClientPacket
import com.vvygulyarniy.l2.loginserver.netty.packet.server.Init
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.util.AttributeKey
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicInteger

@Slf4j
class NettyLoginServerHandler(private val loginController: LoginController,
                              private val packetProcessor: LoginPacketsProcessor) : ChannelInboundHandlerAdapter() {
    private val clientKey = AttributeKey.valueOf<L2LoginClient>("l2LoginClient")
    private var lastSessionId = AtomicInteger(0)

    @Throws(Exception::class)
    override fun channelRegistered(ctx: ChannelHandlerContext) {
        val sessionId = SessionId(lastSessionId.incrementAndGet())
        val crypt = LoginCrypt()

        val client = L2LoginClient(sessionId, NettyLoginClientConnection(ctx), crypt)

        ctx.setSessionId(sessionId)
        ctx.setCrypt(crypt)
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
        val crypt = ctx.getCrypt()

        val initPacket = Init(crypt.scrambledKeyPair.modulus, crypt.blowfishKey, ctx.getSessionId().toInt())

        ctx.channel().writeAndFlush(initPacket)
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(NettyLoginServerHandler::class.java)
    }

}
