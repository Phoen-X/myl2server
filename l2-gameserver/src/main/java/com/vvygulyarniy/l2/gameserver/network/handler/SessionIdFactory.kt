package com.vvygulyarniy.l2.gameserver.network.handler

import com.l2server.network.communication.SessionId
import java.util.concurrent.atomic.AtomicInteger


class SessionIdFactory {
    private val current = AtomicInteger(0)

    fun nextValue() = SessionId(current.incrementAndGet())
}