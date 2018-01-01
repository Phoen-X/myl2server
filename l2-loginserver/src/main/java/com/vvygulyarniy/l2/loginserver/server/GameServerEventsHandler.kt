package com.vvygulyarniy.l2.loginserver.server

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import com.l2server.network.SessionKey
import com.vvygulyarniy.l2.loginserver.communication.CommunicationManager
import com.vvygulyarniy.l2.loginserver.communication.packet.server.PlayOk
import com.vvygulyarniy.l2.loginserver.communication.packet.server.ServerList
import com.vvygulyarniy.l2.loginserver.events.ServerListRequested
import com.vvygulyarniy.l2.loginserver.events.ServerLoginRequested
import org.springframework.stereotype.Component

@Component
class GameServerEventsHandler(eventBus: EventBus,
                              private val gameServersManager: GameServersManager,
                              private val communicationManager: CommunicationManager) {

    init {
        eventBus.register(this)
    }

    @Subscribe
    fun handleServerListRequest(request: ServerListRequested) {
        communicationManager.sendPacket(request.sessionId, ServerList(gameServersManager.getAvailableServers()))
    }

    @Subscribe
    fun handleServerLoginRequest(request: ServerLoginRequested) {
        communicationManager.sendPacket(request.sessionId, PlayOk(SessionKey(1, 2, 3, 4)))
    }
}