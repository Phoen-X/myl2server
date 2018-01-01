package com.vvygulyarniy.l2.loginserver.gameguard

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import com.vvygulyarniy.l2.loginserver.events.GGAuthRequested
import com.vvygulyarniy.l2.loginserver.events.GameGuardAuthSucceeded
import org.springframework.stereotype.Component

@Component
class GameGuardHandler(private val eventBus: EventBus) {
    init {
        eventBus.register(this)
    }

    @Subscribe
    fun handleAuthRequest(request: GGAuthRequested) {
        eventBus.post(GameGuardAuthSucceeded(request.sessionId))
    }

}