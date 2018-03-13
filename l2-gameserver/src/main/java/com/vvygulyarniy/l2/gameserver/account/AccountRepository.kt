package com.vvygulyarniy.l2.gameserver.account

import com.vvygulyarniy.l2.gameserver.domain.AccountId

class AccountRepository {
    private val accounts = mutableMapOf(AccountId(1) to Account(AccountId(1), "asd"))

    fun find(accountId: AccountId): Account? {
        return accounts[accountId]
    }
}
