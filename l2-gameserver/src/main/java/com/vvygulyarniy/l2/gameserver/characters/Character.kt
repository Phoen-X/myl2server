package com.vvygulyarniy.l2.gameserver.characters

import com.vvygulyarniy.l2.gameserver.domain.CharacterId
import com.vvygulyarniy.l2.gameserver.domain.ClassId

data class Character(val id: CharacterId,
                     val name: String,
                     val charClass: ClassId)