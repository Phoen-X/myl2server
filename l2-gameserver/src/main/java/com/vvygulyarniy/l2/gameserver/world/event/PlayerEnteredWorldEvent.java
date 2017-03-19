package com.vvygulyarniy.l2.gameserver.world.event;

import com.vvygulyarniy.l2.gameserver.world.character.L2Player;
import lombok.Getter;

/**
 * Phoen-X on 18.03.2017.
 */
public class PlayerEnteredWorldEvent {
    @Getter
    private final L2Player player;

    public PlayerEnteredWorldEvent(L2Player player) {
        this.player = player;
    }
}
