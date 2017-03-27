package com.vvygulyarniy.l2.gameserver.world.item;

import static com.vvygulyarniy.l2.gameserver.world.character.info.stat.CombatStat.Type.MATK;
import static com.vvygulyarniy.l2.gameserver.world.character.info.stat.CombatStat.Type.PATK;

/**
 * Phoen-X on 21.03.2017.
 */
public class L2Weapon extends L2GenericGearItem {

    public L2Weapon(int id, int itemId, String name, double pAtk, double mAtk) {
        super(id, itemId, name);
        this.bonuses.put(PATK, pAtk);
        this.bonuses.put(MATK, mAtk);

    }

    public double getPatk() {
        return getBonuses().get(PATK);
    }

    public double getMatk() {
        return getBonuses().get(MATK);
    }
}
