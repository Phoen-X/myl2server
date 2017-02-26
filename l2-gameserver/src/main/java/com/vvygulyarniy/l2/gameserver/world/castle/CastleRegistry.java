package com.vvygulyarniy.l2.gameserver.world.castle;

import com.vvygulyarniy.l2.domain.castle.Castle;

import java.util.List;

/**
 * Created by Phoen-X on 25.02.2017.
 */
public interface CastleRegistry {
    List<Castle> findAll();
}
