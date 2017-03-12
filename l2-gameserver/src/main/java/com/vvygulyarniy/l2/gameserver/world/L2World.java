package com.vvygulyarniy.l2.gameserver.world;

import com.vvygulyarniy.l2.domain.geo.Position;
import com.vvygulyarniy.l2.gameserver.network.packet.server.*;
import com.vvygulyarniy.l2.gameserver.world.character.L2Character;
import com.vvygulyarniy.l2.gameserver.world.character.L2Player;
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
public class L2World implements GameEventNotificator {
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(4);
    private final PositionManager positionManager;
    private final int ticksPerSecond;
    private List<L2Player> onlinePlayers = new ArrayList<>();

    public L2World(int ticksPerSecond) {
        this.ticksPerSecond = ticksPerSecond;
        this.positionManager = new PositionManager(this);
        int tickDelay = 1000 / this.ticksPerSecond;
        this.executorService.scheduleAtFixedRate(() -> positionManager.updatePositions(Instant.now()), 0,
                                                 tickDelay, MILLISECONDS);
    }

    public void move(L2Player l2Player, Position moveTo) {
        l2Player.setMoveTarget(moveTo);
        positionManager.startMoving(l2Player, Instant.now());
    }

    public void enterWorld(L2Player player) {
        onlinePlayers.add(player);
        player.send(new UserInfo(player));
        player.send(new ItemList(new ArrayList<>(), false));
        player.send(new ExQuestItemList());
        player.send(new MoveToLocation(player, player.getPosition()));
    }

    public Position validateCharacterPosition(L2Player l2Char, Position position) {
        return positionManager.validatePosition(l2Char, position);
    }

    @Override
    public void notifyMoveFinished(L2Character character) {
        if (character instanceof L2Player) {
            L2Player player = (L2Player) character;
            player.send(new StopMove(player.getId(), player.getPosition()));
        }
    }
}
