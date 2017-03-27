package com.vvygulyarniy.l2.gameserver.world.item;

import com.vvygulyarniy.l2.gameserver.world.character.info.stat.CombatStat;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
public class L2GenericGearItem implements L2GearItem {
    protected final Map<CombatStat.Type, Double> bonuses;
    private final int id;
    private final int itemId;
    private final String name;

    public L2GenericGearItem(int id, int itemId, String name) {
        this.id = id;
        this.itemId = itemId;
        this.name = name;
        this.bonuses = new HashMap<>();
    }

    @Override
    public int getObjectId() {
        return id;
    }

    @Override
    public int getItemId() {
        return itemId;
    }

    @Override
    public int getAugmentationId() {
        return 0;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Map<CombatStat.Type, Double> getBonuses() {
        return bonuses;
    }
}
