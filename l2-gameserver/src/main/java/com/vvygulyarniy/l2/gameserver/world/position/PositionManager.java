package com.vvygulyarniy.l2.gameserver.world.position;

import com.vvygulyarniy.l2.domain.character.L2Character;
import com.vvygulyarniy.l2.domain.geo.Position;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Math.round;
import static java.lang.Math.toIntExact;
import static java.util.stream.Collectors.toList;

/**
 * Phoen-X on 03.03.2017.
 */
@Slf4j
public class PositionManager {
    private final Map<L2Character, MovingContext> movingObjects = new ConcurrentHashMap<>();


    public void updatePositions(Instant now) {
        List<L2Character> finishedMove = movingObjects.keySet()
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

    public void startMoving(L2Character l2Char, Instant now) {
        log.info("Character {} started moving", l2Char);
        MovingContext movingContext = new MovingContext();
        movingContext.setMoveStartTime(now);
        movingContext.setLastUpdate(now);
        movingObjects.put(l2Char, movingContext);
    }

    private void moveChar(L2Character l2Char, MovingContext moveContext, Instant now) {
        log.info("Moving char id: {}, Moving ({}) -> ({})",
                 new Object[]{l2Char.getId(), l2Char.getPosition(), l2Char.getMoveTarget()});
        Position currPosition = l2Char.getPosition();
        Position targetPosition = l2Char.getMoveTarget();

        double pointsPerMillisecond = l2Char.getRunSpeed() / Duration.ofSeconds(1).toMillis();
        long millisSinceLastUpdate = Duration.between(moveContext.getLastUpdate(), now).toMillis();

        double lengthToGo = pointsPerMillisecond * millisSinceLastUpdate;
        if (lengthToGo >= currPosition.distanceTo(targetPosition)) {
            l2Char.setPosition(targetPosition);
            l2Char.moveStopped();
            log.info("Moving stopped for char {}", l2Char);
        } else {
            double yLength = targetPosition.getY() - currPosition.getY();
            double xLength = targetPosition.getX() - currPosition.getX();
            double zLength = targetPosition.getZ() - currPosition.getZ();

            double angleSin = yLength / currPosition.distanceTo(targetPosition);
            double angleCos = xLength / currPosition.distanceTo(targetPosition);
            double zAngleSin = zLength / currPosition.distanceTo(targetPosition);

            double deltaX = lengthToGo * angleCos;
            double deltaY = lengthToGo * angleSin;
            double deltaZ = lengthToGo * zAngleSin;
            int newX = toIntExact(round(currPosition.getX() + deltaX));
            int newY = toIntExact(round(currPosition.getY() + deltaY));
            int newZ = toIntExact(round(currPosition.getZ() + deltaZ));
            Position newPosition = new Position(newX, newY, newZ);
            log.info("New position {}", newPosition);
            l2Char.setPosition(newPosition);
        }
        moveContext.setLastUpdate(now);
    }

    public Position validatePosition(L2Character l2Char, Position proposedPosition) {
        Position currentPosition = l2Char.getPosition();
        if (currentPosition.distanceTo(proposedPosition) < 20) {
            l2Char.setPosition(proposedPosition);
            return proposedPosition;
        } else {
            //TODO we have to make our own opinion, not to trust client side
            l2Char.setPosition(new Position(currentPosition.getX(),
                                            currentPosition.getY(),
                                            proposedPosition.getZ(),
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
