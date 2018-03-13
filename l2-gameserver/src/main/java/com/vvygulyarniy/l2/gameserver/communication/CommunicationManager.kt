package com.vvygulyarniy.l2.gameserver.communication

import com.l2server.network.ServerPacket
import com.l2server.network.communication.ClientCommunicator
import com.l2server.network.communication.SessionId

class CommunicationManager {
    private val communicators = hashMapOf<SessionId, ClientCommunicator>()


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