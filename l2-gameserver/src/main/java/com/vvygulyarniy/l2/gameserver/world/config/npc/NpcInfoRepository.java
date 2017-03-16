package com.vvygulyarniy.l2.gameserver.world.config.npc;

import java.util.List;

/**
 * Phoen-X on 12.03.2017.
 */
public interface NpcInfoRepository {
    List<Npc> findAll();
}
