package com.vvygulyarniy.l2.gameserver.account

import com.vvygulyarniy.l2.gameserver.domain.AccountId


data class Account(val id: AccountId,
                   val login: String)

enum class AccountPlacement {
    CONNECTED, IN_LOBBY, IN_GAME
}