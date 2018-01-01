package com.vvygulyarniy.l2.loginserver.gameguard

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import com.vvygulyarniy.l2.loginserver.communication.CommunicationManager
import com.vvygulyarniy.l2.loginserver.communication.packet.server.GGAuth
import com.vvygulyarniy.l2.loginserver.events.GameGuardAuthSucceeded
import org.springframework.stereotype.Component

@Component
class GameGuardClientNotificator(eventBus: EventBus,
                                 private val communicationManager: CommunicationManager) {

    init {
        eventBus.register(this)
    }

    @Subscribe
    fun notifyGgAuthSucceeded(event: GameGuardAuthSucceeded) {
        communicationManager.sendPacket(event.sessionId, GGAuth(event.sessionId.toInt()))
    }
}