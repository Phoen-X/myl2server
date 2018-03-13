package com.vvygulyarniy.l2.gameserver.events

import com.l2server.network.communication.SessionId
import com.vvygulyarniy.l2.gameserver.domain.AccountId
import com.vvygulyarniy.l2.gameserver.domain.CharacterId

sealed class UserEvent(open val sessionId: SessionId)

data class UserConnected(override val sessionId: SessionId) : UserEvent(sessionId)

data class LobbyEnterRequested(override val sessionId: SessionId, val accountId: AccountId) : UserEvent(sessionId)

data class LobbyQuitRequested(val accountId: AccountId, override val sessionId: SessionId) : UserEvent(sessionId)

data class LobbyCharacterSelected(override val sessionId: SessionId, val accountId: AccountId, val characterId: CharacterId) : UserEvent(sessionId)