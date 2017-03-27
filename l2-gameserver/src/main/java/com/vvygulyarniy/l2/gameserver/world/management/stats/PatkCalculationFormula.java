package com.vvygulyarniy.l2.gameserver.world.management.stats;

import com.vvygulyarniy.l2.gameserver.world.character.L2Player;
import com.vvygulyarniy.l2.gameserver.world.item.L2GearItem;

import static com.vvygulyarniy.l2.gameserver.world.character.info.stat.BasicStat.STR;
import static com.vvygulyarniy.l2.gameserver.world.character.info.stat.CombatStat.Type.PATK;

/**
 * Phoen-X on 19.03.2017.
 */
public class PatkCalculationFormula {
    private final PatkStrDependencyTable strDependency;
    private PatkLvlDependencyTable lvlDependency;

    public PatkCalculationFormula(PatkStrDependencyTable strDependency, PatkLvlDependencyTable lvlDependency) {
        this.strDependency = strDependency;
        this.lvlDependency = lvlDependency;
    }

    public double calculate(L2Player player) {
        double strMultiplier = strDependency.get(player.getBasicStats().get(STR));
        double lvlMultiplier = lvlDependency.calculate(player);
        double weaponMultiplier = resolveWeaponAtkBonus(player);

        return strMultiplier * lvlMultiplier * weaponMultiplier;
    }

    private double resolveWeaponAtkBonus(L2Player player) {
        L2GearItem weaponEqupped = player.getPaperDoll().getRightHand();
        if (weaponEqupped != null) {
            return weaponEqupped.getBonuses().getOrDefault(PATK, defaultWeaponBonus(player));
        } else {
            return defaultWeaponBonus(player);
        }

    }

    private double defaultWeaponBonus(L2Player player) {
        return player.getClassId().isMage() ? 3 : 4;
    }
}
