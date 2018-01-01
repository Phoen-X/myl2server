package com.vvygulyarniy.l2.loginserver.events.router.convert.client

import com.l2server.network.ClientPacket
import com.l2server.network.communication.SessionId
import com.vvygulyarniy.l2.loginserver.communication.packet.client.AuthGameGuard
import com.vvygulyarniy.l2.loginserver.communication.packet.client.RequestAuthLogin
import com.vvygulyarniy.l2.loginserver.communication.packet.client.RequestServerList
import com.vvygulyarniy.l2.loginserver.communication.packet.client.RequestServerLogin
import com.vvygulyarniy.l2.loginserver.events.ClientEvent
import com.vvygulyarniy.l2.loginserver.events.GGAuthRequested
import com.vvygulyarniy.l2.loginserver.events.ServerListRequested
import com.vvygulyarniy.l2.loginserver.events.ServerLoginRequested
import org.springframework.stereotype.Component
import kotlin.reflect.KClass


interface ClientPacketToEventConverter {
    fun canConvert(cls: KClass<out ClientPacket>): Boolean
    fun convert(sessionId: SessionId, packet: ClientPacket): ClientEvent
}

@Component
class AuthGameGuardPacketToEventConverter : ClientPacketToEventConverter {
    override fun canConvert(cls: KClass<out ClientPacket>) = cls == AuthGameGuard::class

    override fun convert(sessionId: SessionId, packet: ClientPacket): ClientEvent {
        val ggAuthPacket = packet as AuthGameGuard
        return GGAuthRequested(SessionId(ggAuthPacket.sessionId))
    }
}

@Component
class RequestServerListPacketToEventConverter : ClientPacketToEventConverter {
    override fun canConvert(cls: KClass<out ClientPacket>) = cls == RequestServerList::class

    override fun convert(sessionId: SessionId, packet: ClientPacket): ClientEvent {
        return ServerListRequested(sessionId)
    }
}

@Component
class RequestAuthLoginPacketToEventConverter : ClientPacketToEventConverter {
    override fun canConvert(cls: KClass<out ClientPacket>) = cls == RequestAuthLogin::class

    override fun convert(sessionId: SessionId, packet: ClientPacket): ClientEvent {
        return ServerListRequested(sessionId)
    }
}

@Component
class RequestServerLoginConverter : ClientPacketToEventConverter {
    override fun canConvert(cls: KClass<out ClientPacket>): Boolean {
        return cls == RequestServerLogin::class
    }

    override fun convert(sessionId: SessionId, packet: ClientPacket): ClientEvent {
        return ServerLoginRequested(sessionId, (packet as RequestServerLogin).serverID)
    }
}