package com.vvygulyarniy.l2.gameserver.characters

import com.google.common.eventbus.Subscribe
import com.l2server.network.communication.SessionId
import com.vvygulyarniy.l2.gameserver.account.AccountRepository
import com.vvygulyarniy.l2.gameserver.account.CharactersRepository
import com.vvygulyarniy.l2.gameserver.communication.CommunicationManager
import com.vvygulyarniy.l2.gameserver.domain.AccountId
import com.vvygulyarniy.l2.gameserver.events.GameEventBus
import com.vvygulyarniy.l2.gameserver.events.PlayerEnteredLobby
import com.vvygulyarniy.l2.gameserver.network.packet.server.CharSelectionInfo
import com.vvygulyarniy.l2.gameserver.network.packet.server.CharSelectionInfo.CharacterInfo


class CharacterInfoManager(private val repo: CharactersRepository,
                           private val accountRepository: AccountRepository,
                           private val communicationManager: CommunicationManager,
                           private val eventBus: GameEventBus) {

    init {
        eventBus.register(this)
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
                                  characters: List<CharacterInfo>,
                                  selectedCharId: Int = -1): CharSelectionInfo {
        return CharSelectionInfo(sessionId.toInt(),
                                 accountRepository.find(accountId)?.login,
                                 characters,
                                 selectedCharId)
    }

    private fun Character.toPacketCharacterInfo(): CharacterInfo {
        return CharacterInfo(this.id.id, 10, this.name, this.charClass)
    }
}