package com.vvygulyarniy.l2.gameserver.account

import com.vvygulyarniy.l2.gameserver.characters.Character
import com.vvygulyarniy.l2.gameserver.domain.AccountId
import com.vvygulyarniy.l2.gameserver.domain.CharacterId
import com.vvygulyarniy.l2.gameserver.domain.ClassId


class CharactersRepository {
    val characters = mutableMapOf(AccountId(1) to listOf(Character(CharacterId(1), "asd", ClassId.abyssWalker)))
    fun findAll(accountId: AccountId): List<Character> {
        return characters[accountId] ?: emptyList()
    }
}