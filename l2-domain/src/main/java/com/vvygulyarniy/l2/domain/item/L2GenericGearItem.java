package com.vvygulyarniy.l2.domain.item;

import lombok.ToString;

/**
 * Created by Phoen-X on 03.03.2017.
 */
@ToString
public class L2GenericGearItem implements L2GearItem {
    private final int id;
    private final int itemId;
    private final String name;
    private final int enchantLvl;

    public L2GenericGearItem(int id, int itemId, String name, int enchantLvl) {
        this.id = id;
        this.itemId = itemId;
        this.name = name;
        this.enchantLvl = enchantLvl;
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
    public int getEnchantLevel() {
        return enchantLvl;
    }

    @Override
    public int getAugmentationId() {
        return 0;
    }

    @Override
    public String name() {
        return name;
    }
}
