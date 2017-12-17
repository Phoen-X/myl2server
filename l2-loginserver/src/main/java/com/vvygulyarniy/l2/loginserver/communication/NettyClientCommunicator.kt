package com.vvygulyarniy.l2.loginserver.communication

import com.vvygulyarniy.l2.loginserver.communication.packet.server.L2LoginServerPacket
import io.netty.channel.ChannelHandlerContext


class NettyClientCommunicator(private val nettyContext: ChannelHandlerContext) : ClientCommunicator {
    override fun send(packet: L2LoginServerPacket) {
        nettyContext.channel().writeAndFlush(packet)
    }

    override fun disconnect() {
        nettyContext.disconnect()
    }
}