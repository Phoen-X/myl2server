package com.vvygulyarniy.l2.gameserver.events

import com.vvygulyarniy.l2.gameserver.domain.AccountId

interface GameEvent

data class PlayerEnteredLobby(val accountId: AccountId) : GameEvent

data class PlayerQuitLobby(val accountId: AccountId) : GameEvent

data class PlayerEnteredWorld(val accountId: AccountId) : GameEvent

