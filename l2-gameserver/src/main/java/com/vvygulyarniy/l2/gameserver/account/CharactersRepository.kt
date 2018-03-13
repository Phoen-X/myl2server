package com.vvygulyarniy.l2.gameserver.account

import com.vvygulyarniy.l2.gameserver.domain.AccountId


class CharactersRepository {
    val characters = mutableMapOf(AccountId(1) to listOf<Character>())
    fun findAll(accountId: AccountId): List<Character> {
        return characters[accountId] ?: emptyList()
    }
}