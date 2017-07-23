package com.vvygulyarniy.l2.loginserver.netty

import com.l2server.network.login.LoginClientConnection
import com.l2server.network.serverpackets.login.L2LoginServerPacket
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
