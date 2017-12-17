package com.vvygulyarniy.l2.loginserver.model.events

import com.vvygulyarniy.l2.loginserver.model.data.SessionId

open class ClientEvent(open val sessionId: SessionId)

data class ClientConnected(override val sessionId: SessionId) : ClientEvent(sessionId)



