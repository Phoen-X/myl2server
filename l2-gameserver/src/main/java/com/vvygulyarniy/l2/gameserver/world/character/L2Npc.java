package com.vvygulyarniy.l2.gameserver.world.character;

import com.vvygulyarniy.l2.gameserver.world.character.info.CollisionParams;
import lombok.Getter;

/**
 * Phoen-X on 12.03.2017.
 */
public class L2Npc extends L2Character {
    @Getter
    private final int npcId;

    public L2Npc(int id, int npcId, String name, int level, CollisionParams collisionParams) {
        super(id, name, level, collisionParams, 0, 100, 100);
        this.npcId = npcId;
    }

    @Override
    public String toString() {
        return String.format("L2Npc(id=%d, npc_id=%d, pos=%s)", id, npcId, position);
    }
}
