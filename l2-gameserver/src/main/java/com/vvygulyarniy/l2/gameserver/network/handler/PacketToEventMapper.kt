package com.vvygulyarniy.l2.gameserver.network.handler

import com.vvygulyarniy.l2.gameserver.events.UserConnected
import com.vvygulyarniy.l2.gameserver.events.UserEvent
import com.vvygulyarniy.l2.gameserver.network.packet.client.L2GameClientPacket
import com.vvygulyarniy.l2.gameserver.network.packet.client.ProtocolVersion
import com.vvygulyarniy.l2.gameserver.session.GameSession


class PacketToEventMapper {
    fun convert(session: GameSession, packet: L2GameClientPacket): UserEvent {
        return when (packet) {
            is ProtocolVersion -> UserConnected(session.sessionId)

            else -> throw RuntimeException("Cannot convert packet to event: $packet. Unmapped equality")
        }
    }
}
