package com.l2server.network.coders.loginserver

import com.l2server.network.and
import com.l2server.network.clientpackets.login.*
import com.l2server.network.login.L2LoginClient
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.util.AttributeKey
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.function.Supplier

class LoginServerClientPacketDecoder : ByteToMessageDecoder() {

    private val clientKey = AttributeKey.valueOf<L2LoginClient>("l2LoginClient")

    @Throws(Exception::class)
    override fun decode(ctx: ChannelHandlerContext, byteBuf: ByteBuf, list: MutableList<Any>) {
        byteBuf.markReaderIndex()
        val dataSize = byteBuf.readShortLE() and 0xFFFF - HEADER_SIZE
        val client = ctx.channel().attr(clientKey).get()
        val byteBuffer = ByteBuffer.allocate(byteBuf.readableBytes()).order(ByteOrder.LITTLE_ENDIAN)
        val data = ByteArray(byteBuf.readableBytes())
        byteBuf.readBytes(data)
        byteBuffer.put(data)
        byteBuffer.position(0)
        val descrypted = client.decrypt(byteBuffer, dataSize)

        if (descrypted && byteBuffer.hasRemaining()) {
            // apply limit
            val limit = byteBuffer.limit()
            byteBuf.capacity(byteBuffer.position() + dataSize)
            val cp = handlePacket(byteBuffer)
            log.info("Packet got: {}", cp)

            if (cp != null) {
                list.add(cp)
            } else {
                byteBuf.resetReaderIndex()
            }

        }
    }

    private fun handlePacket(byteBuffer: ByteBuffer): L2LoginClientPacket? {
        val opcode = byteBuffer.get() and 0xFF

        val packet = opcodeMapping.getOrDefault(opcode, Supplier<L2LoginClientPacket?> { null }).get()

        if (packet == null) {
            return null
        } else {
            packet.buffer = byteBuffer
            if (packet.read()) {
                return packet
            } else {
                return null
            }
        }
    }

    companion object {
        val HEADER_SIZE = 2
        private val opcodeMapping = hashMapOf(
                0x07 to Supplier<L2LoginClientPacket> { AuthGameGuard() },
                0x00 to Supplier<L2LoginClientPacket> { RequestAuthLogin() },
                0x02 to Supplier<L2LoginClientPacket> { RequestServerLogin() },
                0x05 to Supplier<L2LoginClientPacket> { RequestServerList() }
        )
        private val log = org.slf4j.LoggerFactory.getLogger(LoginServerClientPacketDecoder::class.java)
    }
}






