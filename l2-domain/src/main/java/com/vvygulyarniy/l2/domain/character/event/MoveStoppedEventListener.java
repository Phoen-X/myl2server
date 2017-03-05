package com.vvygulyarniy.l2.domain.character.event;

import com.vvygulyarniy.l2.domain.character.L2Character;
import com.vvygulyarniy.l2.domain.geo.Position;
import lombok.Data;

/**
 * Phoen-X on 05.03.2017.
 */
public interface MoveStoppedEventListener {

    void movingStopped(MoveStoppedEvent event);

    @Data
    class MoveStoppedEvent {
        private final L2Character l2Character;
        private final Position lastKnownPosition;
    }
}
