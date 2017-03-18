package com.vvygulyarniy.l2.gameserver.world.event;

import com.vvygulyarniy.l2.gameserver.world.character.L2Character;
import lombok.Getter;

/**
 * Phoen-X on 18.03.2017.
 */
public class CharRegenerated {
    @Getter
    private final L2Character l2Character;

    public CharRegenerated(L2Character l2Char) {
        this.l2Character = l2Char;
    }
}
