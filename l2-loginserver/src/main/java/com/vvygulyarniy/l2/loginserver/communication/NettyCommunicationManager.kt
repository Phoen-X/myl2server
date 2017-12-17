package com.vvygulyarniy.l2.loginserver.communication

import com.vvygulyarniy.l2.loginserver.communication.packet.server.L2LoginServerPacket
import com.vvygulyarniy.l2.loginserver.model.data.SessionId
import io.netty.channel.ChannelHandlerContext
import java.util.concurrent.atomic.AtomicInteger


class NettyCommunicationManager(private val communicatorFactory: (ChannelHandlerContext) -> ClientCommunicator) : CommunicationManager<ChannelHandlerContext> {
    private val lastSessionId = AtomicInteger(0)
    private val communicators = hashMapOf<SessionId, ClientCommunicator>()

    override fun sendPacket(session: SessionId, packet: L2LoginServerPacket) {
        getCommunicator(session).send(packet)
    }

    override fun newChannel(context: ChannelHandlerContext): SessionId {
        val sessionId = SessionId(lastSessionId.incrementAndGet())
        communicators.put(sessionId, communicatorFactory.invoke(context))
        return sessionId
    }

    override fun closeChannel(session: SessionId) {
        getCommunicator(session).disconnect()
    }

    private fun getCommunicator(sessionId: SessionId): ClientCommunicator {
        return communicators.getOrElse(sessionId,
                                       { throw RuntimeException("Communicator for session $sessionId has not been found.") })
    }
}