package com.vvygulyarniy.l2.gameserver.world.management.stats;

import com.google.common.eventbus.Subscribe;
import com.vvygulyarniy.l2.gameserver.network.packet.client.EnterWorld;
import com.vvygulyarniy.l2.gameserver.world.character.L2Player;
import com.vvygulyarniy.l2.gameserver.world.character.info.stat.CombatStat;

import static com.vvygulyarniy.l2.gameserver.world.character.info.stat.CombatStat.Type.PATK;

/**
 * Phoen-X on 19.03.2017.
 */
public class StatCalculator {
    private final PatkCalculationFormula patkFormula;

    public StatCalculator(PatkCalculationFormula patkFormula) {
        this.patkFormula = patkFormula;
    }

    public double calculateCombatStat(L2Player player, CombatStat.Type type) {
        if (type == PATK) {
            return patkFormula.calculate(player);
        }
        return 0;
    }

    @Subscribe
    public void playerEntered(EnterWorld player) {

    }

}
