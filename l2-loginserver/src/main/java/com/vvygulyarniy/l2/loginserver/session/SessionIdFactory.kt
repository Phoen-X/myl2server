package com.vvygulyarniy.l2.loginserver.session

import com.l2server.network.communication.SessionId
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicInteger


@Component
class SessionIdFactory {
    private val lastId = AtomicInteger(0)

    fun generate(): SessionId {
        return SessionId(lastId.incrementAndGet())
    }
}