package com.vvygulyarniy.l2.domain.item;

/**
 * Created by Phoen-X on 03.03.2017.
 */
public interface L2GearItem extends L2Item {
    int getEnchantLevel();

    int getAugmentationId();

    String name();
}
