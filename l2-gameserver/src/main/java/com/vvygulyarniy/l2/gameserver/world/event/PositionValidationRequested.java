package com.vvygulyarniy.l2.gameserver.world.event;

import com.vvygulyarniy.l2.domain.geo.Position;
import com.vvygulyarniy.l2.gameserver.world.character.L2Player;
import lombok.Getter;

/**
 * Phoen-X on 19.03.2017.
 */
public class PositionValidationRequested {
    @Getter
    private final L2Player player;
    @Getter
    private final Position proposedPosition;

    public PositionValidationRequested(L2Player player, Position proposedPosition) {
        this.player = player;
        this.proposedPosition = proposedPosition;
    }
}
