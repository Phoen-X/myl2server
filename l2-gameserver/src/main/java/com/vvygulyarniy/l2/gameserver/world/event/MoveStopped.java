package com.vvygulyarniy.l2.gameserver.world.event;

import com.vvygulyarniy.l2.gameserver.world.character.L2Character;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Phoen-X on 16.03.2017.
 */
@EqualsAndHashCode
public class MoveStopped {
    @Getter
    private final L2Character character;

    public MoveStopped(L2Character character) {
        this.character = character;
    }
}