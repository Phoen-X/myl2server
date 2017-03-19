package com.vvygulyarniy.l2.gameserver.world.position;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vvygulyarniy.l2.domain.geo.Point;
import com.vvygulyarniy.l2.domain.geo.Position;
import com.vvygulyarniy.l2.gameserver.network.packet.server.ValidateLocation;
import com.vvygulyarniy.l2.gameserver.world.character.L2Player;
import com.vvygulyarniy.l2.gameserver.world.event.MoveRequested;
import com.vvygulyarniy.l2.gameserver.world.event.MoveStopped;
import com.vvygulyarniy.l2.gameserver.world.event.PositionValidationRequested;
import com.vvygulyarniy.l2.gameserver.world.time.GameTimeProvider;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Phoen-X on 03.03.2017.
 */
@Slf4j
public class PositionManager {
    private final GameTimeProvider gameTime;
    private final EventBus eventBus;
    private final Map<L2Player, MovingContext> movingObjects = new ConcurrentHashMap<>();

    public PositionManager(GameTimeProvider gameTimeProvider,
                           EventBus eventBus,
                           ScheduledExecutorService scheduler,
                           long tickPeriod,
                           TimeUnit timeUnit) {
        this.gameTime = gameTimeProvider;
        this.eventBus = eventBus;
        eventBus.register(this);
        scheduler.scheduleAtFixedRate(this::updatePositions, 0, tickPeriod, timeUnit);
    }

    public void updatePositions() {
        movingObjects.forEach((l2Char, moveContext) -> moveChar(l2Char, moveContext, gameTime.now()));
    }

    @Subscribe
    public void startMoving(final MoveRequested moveRequest) {
        log.info("Character {} started moving", moveRequest.getPlayer());
        MovingContext movingContext = new MovingContext();
        Instant now = gameTime.now();
        moveRequest.getPlayer().setMoveTarget(moveRequest.getPosition());
        movingContext.setMoveStartTime(now);
        movingContext.setLastUpdate(now);
        movingObjects.put(moveRequest.getPlayer(), movingContext);
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
            l2Char.setMoveTarget(null);
            eventBus.post(new MoveStopped(l2Char));
            movingObjects.remove(l2Char);
            log.info("Moving stopped for char {}", l2Char);
        } else {
            Position newPosition = new Position(current.moveForwardTo(target, lengthToGo),
                                                l2Char.getPosition().getHeading());
            log.info("New position {}", newPosition);
            l2Char.setPosition(newPosition);
        }
        moveContext.setLastUpdate(now);
    }

    @Subscribe
    public void validatePosition(PositionValidationRequested event) {
        L2Player l2Char = event.getPlayer();
        Position proposedPosition = event.getProposedPosition();
        Position currentPosition = l2Char.getPosition();
        Point currentPoint = currentPosition.getPoint();
        Point proposedPoint = proposedPosition.getPoint();
        if (currentPoint.distanceTo(proposedPoint) < 20) {
            l2Char.setPosition(proposedPosition);
        } else {
            //TODO we have to make our own opinion, not to trust client side
            l2Char.setPosition(new Position(new Point(currentPoint.getX(),
                                                      currentPoint.getY(),
                                                      proposedPoint.getZ()),
                                            proposedPosition.getHeading()));
        }
        l2Char.send(new ValidateLocation(l2Char.getId(), l2Char.getPosition()));
    }

    @Data
    private static final class MovingContext {
        private Instant moveStartTime;
        private Instant lastUpdate;
    }
}
