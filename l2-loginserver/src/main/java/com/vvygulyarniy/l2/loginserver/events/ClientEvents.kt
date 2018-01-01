package com.vvygulyarniy.l2.loginserver.events

import com.l2server.network.communication.SessionId

open class ClientEvent

data class GGAuthRequested(val sessionId: SessionId) : ClientEvent()

data class ServerListRequested(val sessionId: SessionId) : ClientEvent()

data class ServerLoginRequested(val sessionId: SessionId, val serverId: Byte) : ClientEvent()



