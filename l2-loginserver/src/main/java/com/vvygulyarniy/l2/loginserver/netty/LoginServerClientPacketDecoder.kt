package com.vvygulyarniy.l2.loginserver.netty

import com.vvygulyarniy.l2.loginserver.netty.login.L2LoginClient
import com.vvygulyarniy.l2.loginserver.netty.packet.client.*
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.util.AttributeKey
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.experimental.and

class LoginServerClientPacketDecoder : ByteToMessageDecoder() {

    private val clientKey = AttributeKey.valueOf<L2LoginClient>("l2LoginClient")

    @Throws(Exception::class)
    override fun decode(ctx: ChannelHandlerContext, byteBuf: ByteBuf, list: MutableList<Any>) {
        byteBuf.markReaderIndex()
        val dataSize = byteBuf.readShortLE().toInt() and 0xFFFF - HEADER_SIZE
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
        val opcode = byteBuffer.get().toShort() and 0xFF

        val packet = opcodeMapping.getOrDefault(opcode, { null }).invoke()

        return if (packet != null) {
            packet.buffer = byteBuffer
            if (packet.read()) packet else null
        } else {
            null
        }
    }

    companion object {
        val HEADER_SIZE = 2
        private val authGgOpcode: Short = 0x07
        private val requestAuthOpCode: Short = 0x00
        private val requestServerLoginOpCode: Short = 0x02
        private val requestServerListOpCode: Short = 0x05

        private val opcodeMapping: HashMap<Short, () -> L2LoginClientPacket?> = hashMapOf(
                authGgOpcode to ::AuthGameGuard,
                requestAuthOpCode to ::RequestAuthLogin,
                requestServerLoginOpCode to ::RequestServerLogin,
                requestServerListOpCode to ::RequestServerList
        )

        private val log = org.slf4j.LoggerFactory.getLogger(LoginServerClientPacketDecoder::class.java)
    }
}






