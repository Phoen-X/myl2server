package com.vvygulyarniy.l2.gameserver.world.management.stats;

import com.vvygulyarniy.l2.gameserver.world.character.L2Player;

/**
 * Phoen-X on 19.03.2017.
 */
public class PatkLvlDependencyTable {

    public double calculate(L2Player player) {
        return (player.getLevel() + 89) / 100.0;
    }
}
