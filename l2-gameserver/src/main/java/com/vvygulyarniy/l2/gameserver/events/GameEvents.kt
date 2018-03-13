package com.vvygulyarniy.l2.gameserver.events

import com.l2server.network.communication.SessionId
import com.vvygulyarniy.l2.gameserver.domain.AccountId

interface GameEvent

data class PlayerEnteredLobby(val sessionId: SessionId, val accountId: AccountId) : GameEvent

data class PlayerQuitLobby(val sessionId: SessionId, val accountId: AccountId) : GameEvent

data class PlayerEnteredWorld(val sessionId: SessionId, val accountId: AccountId) : GameEvent

