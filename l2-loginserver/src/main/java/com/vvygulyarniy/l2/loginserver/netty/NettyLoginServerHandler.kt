package com.vvygulyarniy.l2.loginserver.netty

import com.l2server.crypt.LoginCrypt
import com.vvygulyarniy.l2.loginserver.communication.CommunicationManager
import com.vvygulyarniy.l2.loginserver.communication.packet.client.L2LoginClientPacket
import com.vvygulyarniy.l2.loginserver.communication.packet.server.Init
import com.vvygulyarniy.l2.loginserver.logic.LoginPacketsProcessor
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Slf4j
class NettyLoginServerHandler(private val communicationManager: CommunicationManager<ChannelHandlerContext>,
                              private val packetProcessor: LoginPacketsProcessor<ChannelHandlerContext>) : ChannelInboundHandlerAdapter() {
    @Throws(Exception::class)
    override fun channelRegistered(ctx: ChannelHandlerContext) {
        val sessionId = communicationManager.newChannel(ctx)
        val crypt = LoginCrypt()

        ctx.setSessionId(sessionId)
        ctx.setCrypt(crypt)
        log.info("Channel registered: {}", ctx)
    }

    @Throws(Exception::class)
    override fun channelUnregistered(ctx: ChannelHandlerContext) {
    }

    @Throws(Exception::class)
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        log.info("Got message: {}", msg)
        val packet = msg as L2LoginClientPacket
        packetProcessor.process(ctx.getSessionId(), packet)
    }

    @Throws(Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext) {
        log.info("Channel active: {}", ctx)
        val crypt = ctx.getCrypt()

        val initPacket = Init(crypt.scrambledKeyPair.modulus, crypt.blowfishKey, ctx.getSessionId().toInt())

        communicationManager.sendPacket(ctx.getSessionId(), initPacket)
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(NettyLoginServerHandler::class.java)
    }

}
