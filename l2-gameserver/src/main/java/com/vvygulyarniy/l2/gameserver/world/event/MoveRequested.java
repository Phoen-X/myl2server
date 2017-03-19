package com.vvygulyarniy.l2.gameserver.world.event;

import com.vvygulyarniy.l2.domain.geo.Position;
import com.vvygulyarniy.l2.gameserver.world.character.L2Player;
import lombok.Getter;

/**
 * Phoen-X on 19.03.2017.
 */
@Getter
public class MoveRequested {
    private final L2Player player;
    private final Position position;

    public MoveRequested(L2Player player, Position position) {
        this.player = player;
        this.position = position;
    }
}
