package com.vvygulyarniy.l2.gameserver.world.event.npc;

import com.vvygulyarniy.l2.gameserver.world.character.L2Npc;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Phoen-X on 18.03.2017.
 */
@ToString
@EqualsAndHashCode
public class NpcSpawned {
    @Getter
    private final L2Npc npc;

    public NpcSpawned(L2Npc npc) {
        this.npc = npc;
    }
}
