package com.vvygulyarniy.l2.loginserver.netty

import com.l2server.network.login.L2LoginClient
import com.l2server.network.serverpackets.login.L2LoginServerPacket
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import io.netty.util.AttributeKey
import java.nio.ByteBuffer
import java.nio.ByteOrder.LITTLE_ENDIAN

class LoginServerPacketEncoder : MessageToByteEncoder<L2LoginServerPacket>() {

    override fun encode(ctx: ChannelHandlerContext, packet: L2LoginServerPacket, out: ByteBuf) {
        log.info("Encoding message: {}", packet)
        val client = getClient(ctx)

        val buffer = ByteBuffer.allocate(65000).order(LITTLE_ENDIAN)
        val headerPos = 0

        val dataPos = headerPos + 2
        buffer.position(dataPos)

        packet.write(buffer)

        var dataSize = buffer.position() - dataPos
        buffer.position(dataPos)
        client.loginCrypt.encrypt(buffer, dataSize)

        // recalculate size after encryption
        dataSize = buffer.position() - dataPos

        buffer.position(headerPos)
        // write header
        buffer.putShort((dataSize + 2).toShort())
        buffer.position(dataPos + dataSize)
        val data = bufferData(buffer)

        out.writeBytes(data)
        log.info("Packet sent {}", packet)
    }

    private fun getClient(ctx: ChannelHandlerContext): L2LoginClient {
        return ctx.channel().attr(AttributeKey.valueOf<L2LoginClient>("l2LoginClient")).get()
    }

    @Throws(Exception::class)
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        log.error("Got exception while encoding packet", cause)
    }

    companion object {

        private val log = org.slf4j.LoggerFactory.getLogger(LoginServerPacketEncoder::class.java)

        fun bufferData(buffer: ByteBuffer): ByteArray {
            val lastPos = buffer.position()
            buffer.position(0)
            val data = ByteArray(lastPos)
            buffer.get(data)
            buffer.position(lastPos)
            return data
        }
    }
}
