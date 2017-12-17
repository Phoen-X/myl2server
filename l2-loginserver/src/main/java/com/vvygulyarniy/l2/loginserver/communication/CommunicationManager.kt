package com.vvygulyarniy.l2.loginserver.communication

import com.vvygulyarniy.l2.loginserver.communication.packet.server.L2LoginServerPacket
import com.vvygulyarniy.l2.loginserver.model.data.SessionId


interface CommunicationManager<in T : Any> {
    fun sendPacket(session: SessionId, packet: L2LoginServerPacket)
    fun newChannel(context: T): SessionId
    fun closeChannel(session: SessionId)
}