package com.vvygulyarniy.l2.gameserver.world.item;

import com.vvygulyarniy.l2.gameserver.world.character.info.stat.CombatStat.Type;

import java.util.Map;

/**
 * Created by Phoen-X on 03.03.2017.
 */
public interface L2GearItem extends L2Item {
    int getAugmentationId();

    String name();

    Map<Type, Double> getBonuses();
}
