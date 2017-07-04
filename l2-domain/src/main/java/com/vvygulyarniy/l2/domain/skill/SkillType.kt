package com.vvygulyarniy.l2.domain.skill

enum class SkillType {
    CLASS,
    FISHING,
    PLEDGE,
    SUBPLEDGE,
    TRANSFORM,
    TRANSFER,
    SUBCLASS,
    COLLECT;


    companion object {
        @JvmStatic fun getSkillType(id: Int): SkillType {
            return values()[id]
        }
    }
}
