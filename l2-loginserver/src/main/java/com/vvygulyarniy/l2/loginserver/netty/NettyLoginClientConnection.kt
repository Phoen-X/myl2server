package com.vvygulyarniy.l2.loginserver.netty

import com.vvygulyarniy.l2.loginserver.communication.packet.server.L2LoginServerPacket
import com.vvygulyarniy.l2.loginserver.netty.login.LoginClientConnection
import io.netty.channel.ChannelHandlerContext

/**
 * Phoen-X on 04.07.2017.
 */
class NettyLoginClientConnection internal constructor(private val ctx: ChannelHandlerContext) : LoginClientConnection {

    override fun send(packet: L2LoginServerPacket) {
        ctx.channel().writeAndFlush(packet)
    }

    override fun disconnect() {
        ctx.disconnect()
    }
}
