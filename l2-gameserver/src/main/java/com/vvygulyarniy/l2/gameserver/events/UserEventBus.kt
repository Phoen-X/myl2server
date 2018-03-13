package com.vvygulyarniy.l2.gameserver.events

import com.google.common.eventbus.EventBus
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class UserEventBus {
    private val bus = EventBus("user_events")

    fun register(handler: Any) {
        bus.register(handler)
    }

    fun post(event: UserEvent) {
        bus.post(event)
        log.info("User event issued: $event")
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(UserEventBus::class.java)
    }
}