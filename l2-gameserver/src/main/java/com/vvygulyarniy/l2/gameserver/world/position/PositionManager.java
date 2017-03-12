package com.vvygulyarniy.l2.gameserver.world.position;

import com.vvygulyarniy.l2.domain.geo.Point;
import com.vvygulyarniy.l2.domain.geo.Position;
import com.vvygulyarniy.l2.gameserver.world.GameEventNotificator;
import com.vvygulyarniy.l2.gameserver.world.character.L2Player;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toList;

/**
 * Phoen-X on 03.03.2017.
 */
@Slf4j
public class PositionManager {
    private final GameEventNotificator notificator;
    private final Map<L2Player, MovingContext> movingObjects = new ConcurrentHashMap<>();

    public PositionManager(GameEventNotificator notificator) {
        this.notificator = notificator;
    }

    public void updatePositions(Instant now) {
        List<L2Player> finishedMove = movingObjects.keySet()
                                                   .stream()
                                                   .filter(l2char -> l2char.getMoveTarget() == null || Objects.equals(
                                                              l2char.getMoveTarget(),
                                                              l2char.getPosition()))
                                                   .collect(toList());

        finishedMove.forEach(l2char -> {
            log.info("Removing char {} from moving list", l2char.getId());
            movingObjects.remove(l2char);
        });
        movingObjects.forEach((l2Char, moveContext) -> moveChar(l2Char, moveContext, now));
    }

    public void startMoving(L2Player l2Char, Instant now) {
        log.info("Character {} started moving", l2Char);
        MovingContext movingContext = new MovingContext();
        movingContext.setMoveStartTime(now);
        movingContext.setLastUpdate(now);
        movingObjects.put(l2Char, movingContext);
    }

    private void moveChar(L2Player l2Char, MovingContext moveContext, Instant now) {
        log.info("Moving char id: {}, Moving ({}) -> ({})",
                 new Object[]{l2Char.getId(), l2Char.getPosition(), l2Char.getMoveTarget()});
        Point current = l2Char.getPosition().getPoint();
        Point target = l2Char.getMoveTarget().getPoint();

        double pointsPerMillisecond = l2Char.getRunSpeed() / Duration.ofSeconds(1).toMillis();
        long millisSinceLastUpdate = Duration.between(moveContext.getLastUpdate(), now).toMillis();

        double lengthToGo = pointsPerMillisecond * millisSinceLastUpdate;
        if (lengthToGo >= current.distanceTo(target)) {
            l2Char.setPosition(new Position(target, l2Char.getPosition().getHeading()));
            l2Char.moveStopped();
            log.info("Moving stopped for char {}", l2Char);
        } else {
            Position newPosition = new Position(current.moveForwardTo(target, lengthToGo),
                                                l2Char.getPosition().getHeading());
            log.info("New position {}", newPosition);
            l2Char.setPosition(newPosition);
        }
        moveContext.setLastUpdate(now);
    }

    public Position validatePosition(L2Player l2Char, Position proposedPosition) {
        Position currentPosition = l2Char.getPosition();
        Point currentPoint = currentPosition.getPoint();
        Point proposedPoint = proposedPosition.getPoint();
        if (currentPoint.distanceTo(proposedPoint) < 20) {
            l2Char.setPosition(proposedPosition);
            return proposedPosition;
        } else {
            //TODO we have to make our own opinion, not to trust client side
            l2Char.setPosition(new Position(new Point(currentPoint.getX(),
                                                      currentPoint.getY(),
                                                      proposedPoint.getZ()),
                                            proposedPosition.getHeading()));
            return l2Char.getPosition();
        }
    }

    @Data
    private static final class MovingContext {
        private Instant moveStartTime;
        private Instant lastUpdate;
    }
}
