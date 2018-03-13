package com.vvygulyarniy.l2.gameserver.lobby

import com.google.common.eventbus.Subscribe
import com.vvygulyarniy.l2.gameserver.domain.AccountId
import com.vvygulyarniy.l2.gameserver.events.*

class LobbyManager(userEventBus: UserEventBus, private val gameEventBus: GameEventBus) {
    private val accountsInLobby = mutableListOf<AccountId>()

    init {
        userEventBus.register(this)
    }

    @Subscribe
    fun lobbyEnterRequested(event: LobbyEnterRequested) {
        val accountId = event.accountId

        if (!accountsInLobby.contains(accountId)) {
            accountsInLobby.add(accountId)
            gameEventBus.post(PlayerEnteredLobby(event.sessionId, accountId))
        }
    }

    @Subscribe
    fun lobbyQuitRequested(event: LobbyQuitRequested) {
        val accountId = event.accountId

        accountsInLobby.remove(accountId)
        gameEventBus.post(PlayerQuitLobby(event.sessionId, accountId))
    }
}