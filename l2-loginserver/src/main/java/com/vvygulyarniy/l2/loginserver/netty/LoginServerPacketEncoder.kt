package com.vvygulyarniy.l2.loginserver.netty

import com.vvygulyarniy.l2.loginserver.communication.packet.server.L2LoginServerPacket
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import java.nio.ByteBuffer
import java.nio.ByteOrder.LITTLE_ENDIAN

class LoginServerPacketEncoder : MessageToByteEncoder<L2LoginServerPacket>() {

    override fun encode(ctx: ChannelHandlerContext, packet: L2LoginServerPacket, out: ByteBuf) {
        log.info("Encoding message: {}", packet)

        val buffer = ByteBuffer.allocate(65000).order(LITTLE_ENDIAN)
        val headerPos = 0

        val dataPos = headerPos + 2
        buffer.position(dataPos)

        packet.write(buffer)

        var dataSize = buffer.position() - dataPos
        buffer.position(dataPos)
        ctx.getCrypt().encrypt(buffer, dataSize)

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
