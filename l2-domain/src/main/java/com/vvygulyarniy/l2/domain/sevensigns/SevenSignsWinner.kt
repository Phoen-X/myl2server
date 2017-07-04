package com.vvygulyarniy.l2.domain.sevensigns

enum class SevenSignsWinner {
    NONE, DUSK, DAWN;

    val id: Int
        get() = this.ordinal
}
