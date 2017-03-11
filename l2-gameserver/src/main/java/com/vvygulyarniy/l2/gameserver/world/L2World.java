package com.vvygulyarniy.l2.gameserver.world;

import com.vvygulyarniy.l2.domain.character.L2Player;
import com.vvygulyarniy.l2.domain.geo.Position;
import com.vvygulyarniy.l2.gameserver.world.position.PositionManager;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Created by Phoen-X on 03.03.2017.
 */
public class L2World {
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(4);
    private final PositionManager positionManager;
    private final int ticksPerSecond;
    private List<L2Player> onlineCharacters = new ArrayList<>();

    public L2World(int ticksPerSecond) {
        this.ticksPerSecond = ticksPerSecond;
        this.positionManager = new PositionManager();
        int tickDelay = 1000 / this.ticksPerSecond;
        this.executorService.scheduleAtFixedRate(() -> positionManager.updatePositions(Instant.now()), 0,
                                                 tickDelay, MILLISECONDS);
    }

    public void move(L2Player l2Player, Position moveTo) {
        l2Player.setMoveTarget(moveTo);
        positionManager.startMoving(l2Player, Instant.now());
    }

    public void addCharacter(L2Player character) {
        onlineCharacters.add(character);
    }

    public Position validateCharacterPosition(L2Player l2Char, Position position) {
        return positionManager.validatePosition(l2Char, position);
    }
}
