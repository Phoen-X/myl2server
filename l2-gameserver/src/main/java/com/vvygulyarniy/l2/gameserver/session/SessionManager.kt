package com.vvygulyarniy.l2.gameserver.session

import com.l2server.network.communication.SessionId
import com.vvygulyarniy.l2.gameserver.network.handler.SessionIdFactory
import java.time.LocalDateTime


class SessionManager(private val sessionIdFactory: SessionIdFactory) {
    private val sessions = hashMapOf<SessionId, GameSession>()

    fun startSession(): SessionId {
        val session = buildNewSession()
        sessions[session.sessionId] = session
        return session.sessionId
    }

    fun closeSession(sessionId: SessionId) {
        sessions.remove(sessionId)
    }

    private fun buildNewSession(): GameSession {
        val sessionId = sessionIdFactory.nextValue()
        return GameSession(sessionId, LocalDateTime.now())
    }

    fun getSession(sessionId: SessionId): GameSession? {
        return sessions[sessionId]
    }
}