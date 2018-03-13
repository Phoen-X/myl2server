package com.vvygulyarniy.l2.gameserver.session

import com.google.common.eventbus.Subscribe
import com.vvygulyarniy.l2.gameserver.communication.CommunicationManager
import com.vvygulyarniy.l2.gameserver.crypt.CryptService
import com.vvygulyarniy.l2.gameserver.events.UserConnected
import com.vvygulyarniy.l2.gameserver.events.UserEventBus
import com.vvygulyarniy.l2.gameserver.network.packet.server.KeyPacket

class HandshakeHandler(eventBus: UserEventBus,
                       private val communicationManager: CommunicationManager,
                       private val cryptService: CryptService) {
    init {
        eventBus.register(this)
    }

    @Subscribe
    fun handleHandshake(event: UserConnected) {
        val cryptKey = cryptService.getKey(event.sessionId)
        communicationManager.sendPacket(event.sessionId, KeyPacket(cryptKey, 1))
    }
}
