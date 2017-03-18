package com.vvygulyarniy.l2.gameserver.world.event;

import com.vvygulyarniy.l2.gameserver.world.character.L2Character;
import lombok.Getter;

/**
 * Phoen-X on 18.03.2017.
 */
public class DamageReceived {
    @Getter
    private final L2Character character;

    public DamageReceived(L2Character character) {
        this.character = character;
    }
}
