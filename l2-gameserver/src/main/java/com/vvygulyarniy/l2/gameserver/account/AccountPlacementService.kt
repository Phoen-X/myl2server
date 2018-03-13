package com.vvygulyarniy.l2.gameserver.account

import com.google.common.eventbus.Subscribe
import com.vvygulyarniy.l2.gameserver.account.AccountPlacement.*
import com.vvygulyarniy.l2.gameserver.domain.AccountId
import com.vvygulyarniy.l2.gameserver.events.GameEventBus
import com.vvygulyarniy.l2.gameserver.events.PlayerEnteredLobby
import com.vvygulyarniy.l2.gameserver.events.PlayerEnteredWorld
import com.vvygulyarniy.l2.gameserver.events.PlayerQuitLobby

class AccountPlacementService(gameEventBus: GameEventBus) {
    private val placementRegistry = hashMapOf<AccountId, AccountPlacement>()

    init {
        gameEventBus.register(this)
    }

    fun getCurrentPlacement(accountId: AccountId?) = placementRegistry.getOrDefault(accountId, CONNECTED)

    @Subscribe
    fun playerEnteredLobby(event: PlayerEnteredLobby) {
        placementRegistry[event.accountId] = IN_LOBBY
    }

    @Subscribe
    fun playerQuitLobby(event: PlayerQuitLobby) {
        placementRegistry[event.accountId] = CONNECTED
    }

    @Subscribe
    fun playerEnteredWorld(event: PlayerEnteredWorld) {
        placementRegistry[event.accountId] = IN_GAME
    }
}
