package com.vvygulyarniy.l2.loginserver.events.router

import com.google.common.eventbus.EventBus
import com.l2server.network.ClientPacket
import com.l2server.network.communication.SessionId
import com.vvygulyarniy.l2.loginserver.events.router.convert.client.ClientPacketToEventConverter
import org.springframework.stereotype.Component


@Component
class ClientPacketsRouter(private val eventBus: EventBus,
                          private val knownConverterClients: List<ClientPacketToEventConverter>) {

    fun handlePacket(sessionId: SessionId, packet: ClientPacket) {
        val packetClass = packet::class
        val converter = knownConverterClients.find { it.canConvert(packetClass) }
        if (converter != null) {
            eventBus.post(converter.convert(sessionId, packet))
        } else {
            throw RuntimeException("Cannot resolve event fitting packet $packet")
        }
    }
}