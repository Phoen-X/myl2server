package com.vvygulyarniy.l2.gameserver.lobby

import com.google.common.eventbus.Subscribe
import com.l2server.network.communication.SessionId
import com.vvygulyarniy.l2.gameserver.account.AccountRepository
import com.vvygulyarniy.l2.gameserver.characters.Character
import com.vvygulyarniy.l2.gameserver.characters.CharactersRepository
import com.vvygulyarniy.l2.gameserver.communication.CommunicationManager
import com.vvygulyarniy.l2.gameserver.domain.AccountId
import com.vvygulyarniy.l2.gameserver.events.*
import com.vvygulyarniy.l2.gameserver.network.packet.server.CharSelectionInfo

class LobbyManager(private val repo: CharactersRepository,
                   private val accountRepository: AccountRepository,
                   private val communicationManager: CommunicationManager,
                   private val userEventBus: UserEventBus, private val gameEventBus: GameEventBus) {
    private val accountsInLobby = mutableListOf<AccountId>()

    init {
        userEventBus.register(this)
        gameEventBus.register(this)
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

    @Subscribe
    fun lobbyEntered(event: PlayerEnteredLobby) {
        val characters = repo.findAll(event.accountId)
                .map { it.toPacketCharacterInfo() }

        communicationManager.sendPacket(event.sessionId,
                                        charSelectionInfo(event.sessionId, event.accountId, characters))
    }

    private fun charSelectionInfo(sessionId: SessionId,
                                  accountId: AccountId,
                                  characters: List<CharSelectionInfo.CharacterInfo>,
                                  selectedCharId: Int = -1): CharSelectionInfo {
        return CharSelectionInfo(sessionId.id,
                                 accountRepository.find(accountId)?.login,
                                 characters,
                                 selectedCharId)
    }

    private fun Character.toPacketCharacterInfo(): CharSelectionInfo.CharacterInfo {
        return CharSelectionInfo.CharacterInfo(this.id.id, 10, this.name, this.charClass)
    }

}