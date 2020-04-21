package com.l2server.network.communication

import com.l2server.network.ServerPacket
import io.netty.channel.ChannelHandlerContext


class NettyClientCommunicator(private val nettyContext: ChannelHandlerContext) : ClientCommunicator {
    override fun send(packet: ServerPacket) {
        nettyContext.channel().writeAndFlush(packet)
    }

    override fun disconnect() {
        nettyContext.disconnect()
    }
}