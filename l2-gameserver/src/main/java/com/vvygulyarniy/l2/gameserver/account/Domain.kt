package com.vvygulyarniy.l2.gameserver.account

import com.vvygulyarniy.l2.gameserver.domain.AccountId
import com.vvygulyarniy.l2.gameserver.domain.CharacterId
import com.vvygulyarniy.l2.gameserver.domain.ClassId


data class Account(val id: AccountId,
                   val login: String)

data class Character(val id: CharacterId,
                     val charClass: ClassId)

enum class AccountPlacement {
    CONNECTED, IN_LOBBY, IN_GAME
}