package com.vvygulyarniy.l2.domain.skill;

/**
 * Created by Phoen-X on 25.02.2017.
 */
public enum SkillType {
    CLASS,
    FISHING,
    PLEDGE,
    SUBPLEDGE,
    TRANSFORM,
    TRANSFER,
    SUBCLASS,
    COLLECT;

    public static SkillType getSkillType(int id) {
        return values()[id];
    }
}
