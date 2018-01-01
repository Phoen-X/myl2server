package com.vvygulyarniy.l2.loginserver.events

import com.l2server.network.communication.SessionId


open class ServerEvent(open val sessionId: SessionId)

data class GameGuardAuthSucceeded(override val sessionId: SessionId) : ServerEvent(sessionId)