package com.vvygulyarniy.l2.loginserver.netty

import com.l2server.crypt.LoginCrypt
import com.l2server.network.ClientPacket
import com.l2server.network.communication.NettyClientCommunicator
import com.vvygulyarniy.l2.loginserver.communication.CommunicationManager
import com.vvygulyarniy.l2.loginserver.communication.packet.server.Init
import com.vvygulyarniy.l2.loginserver.events.router.ClientPacketsRouter
import com.vvygulyarniy.l2.loginserver.session.SessionIdFactory
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class NettyLoginServerHandler(private val communicationManager: CommunicationManager,
                              private val sessionFactory: SessionIdFactory,
                              private val packetProcessor: ClientPacketsRouter) : ChannelInboundHandlerAdapter() {

    override fun channelRegistered(ctx: ChannelHandlerContext) {
        val crypt = LoginCrypt()
        val sessionId = sessionFactory.generate()
        ctx.setSessionId(sessionId)
        ctx.setCrypt(crypt)

        communicationManager.newChannel(sessionId, NettyClientCommunicator(ctx))
        log.info("Channel registered: {}", ctx)
    }

    @Throws(Exception::class)
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        log.info("Got message: {}", msg)
        val packet = msg as ClientPacket
        packetProcessor.handlePacket(ctx.getSessionId(), packet)
    }

    @Throws(Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext) {
        log.info("Channel active: {}", ctx)
        val crypt = ctx.getCrypt()

        val initPacket = Init(crypt.scrambledKeyPair.modulus, crypt.blowfishKey, ctx.getSessionId().toInt())

        communicationManager.sendPacket(ctx.getSessionId(), initPacket)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
        log.error("Exception got while processing user request", cause)
        ctx?.let { communicationManager.closeChannel(it.getSessionId()) }
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(NettyLoginServerHandler::class.java)
    }

}
