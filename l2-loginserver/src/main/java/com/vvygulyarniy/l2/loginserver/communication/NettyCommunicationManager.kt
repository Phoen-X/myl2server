package com.vvygulyarniy.l2.loginserver.communication

import com.google.common.eventbus.EventBus
import com.l2server.network.ServerPacket
import com.l2server.network.communication.ClientCommunicator
import com.l2server.network.communication.SessionId
import org.springframework.stereotype.Component

@Component
class CommunicationManager(eventBus: EventBus) {
    private val communicators = hashMapOf<SessionId, ClientCommunicator>()

    init {
        eventBus.register(this)
    }

    fun sendPacket(session: SessionId, packet: ServerPacket) {
        getCommunicator(session).send(packet)
    }

    fun newChannel(sessionId: SessionId, communicator: ClientCommunicator) {
        communicators.put(sessionId, communicator)
    }

    fun closeChannel(session: SessionId) {
        getCommunicator(session).disconnect()
    }

    private fun getCommunicator(sessionId: SessionId): ClientCommunicator {
        return communicators.getOrElse(sessionId,
                                       { throw RuntimeException("Communicator for session $sessionId has not been found.") })
    }
}