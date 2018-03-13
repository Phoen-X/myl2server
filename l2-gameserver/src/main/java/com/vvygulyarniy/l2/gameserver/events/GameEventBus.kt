package com.vvygulyarniy.l2.gameserver.events

import com.google.common.eventbus.EventBus
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class GameEventBus {
    private val bus = EventBus("game_events")

    fun register(handler: Any) {
        bus.register(handler)
    }

    fun post(event: GameEvent) {
        bus.post(event)
        log.info("Game event issued: $event")
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(GameEventBus::class.java)
    }
}