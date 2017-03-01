package com.vvygulyarniy.l2.gameserver.world.castle;

import com.vvygulyarniy.l2.domain.castle.Castle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Phoen-X on 25.02.2017.
 */
public class HardCodedCastleRegistry implements CastleRegistry {
    private final List<Castle> castles = Arrays.asList(
            new Castle(1, "Gludio"),
            new Castle(2, "Dion"),
            new Castle(3, "Giran"),
            new Castle(4, "Oren"),
            new Castle(5, "Aden"),
            new Castle(6, "Innadril"),
            new Castle(7, "Goddard"),
            new Castle(8, "Rune"),
            new Castle(9, "Schuttgart")
    );

    @Override
    public List<Castle> findAll() {
        return new ArrayList<>(castles);
    }
}
