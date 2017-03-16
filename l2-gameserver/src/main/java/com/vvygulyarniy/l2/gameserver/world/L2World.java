package com.vvygulyarniy.l2.gameserver.world;

import com.vvygulyarniy.l2.domain.geo.Position;
import com.vvygulyarniy.l2.gameserver.network.packet.server.AbstractNpcInfo.NpcInfo;
import com.vvygulyarniy.l2.gameserver.network.packet.server.*;
import com.vvygulyarniy.l2.gameserver.world.character.L2Character;
import com.vvygulyarniy.l2.gameserver.world.character.L2Npc;
import com.vvygulyarniy.l2.gameserver.world.character.L2Player;
import com.vvygulyarniy.l2.gameserver.world.config.npc.XmlNpcInfoRepository;
import com.vvygulyarniy.l2.gameserver.world.config.npc.XmlNpcSpawnInfoParser;
import com.vvygulyarniy.l2.gameserver.world.npc.NpcSpawnManager;
import com.vvygulyarniy.l2.gameserver.world.position.PositionManager;
import lombok.extern.slf4j.Slf4j;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Created by Phoen-X on 03.03.2017.
 */
@Slf4j
public class L2World implements GameEventNotificator {
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(4);
    private final PositionManager positionManager;
    private final NpcSpawnManager spawnManager;
    private final int ticksPerSecond;
    private List<L2Player> onlinePlayers = new ArrayList<>();
    private List<L2Npc> npcList = new ArrayList<>();

    public L2World(int ticksPerSecond) throws JDOMException, IOException, URISyntaxException {
        this.ticksPerSecond = ticksPerSecond;
        this.positionManager = new PositionManager(this);
        Path npcInfoFile = Paths.get(ClassLoader.getSystemResource("npc_info.xml").toURI());
        this.spawnManager = new NpcSpawnManager(this, new XmlNpcInfoRepository(new XmlNpcSpawnInfoParser(npcInfoFile)));
        int tickDelay = 1000 / this.ticksPerSecond;
        this.executorService.scheduleAtFixedRate(() -> positionManager.updatePositions(Instant.now()), 0,
                                                 tickDelay, MILLISECONDS);
        this.executorService.scheduleAtFixedRate(spawnManager::spawnNpcs, 10000, tickDelay, MILLISECONDS);
        this.executorService.scheduleAtFixedRate(this::notifyNpcs, 0, tickDelay, MILLISECONDS);
    }

    private void notifyNpcs() {
        log.info("Notifying about NPCs");
        log.info("Online players: {}", onlinePlayers.size());
        log.info("Alive NPCs: {}", npcList);
        //onlinePlayers.forEach(player -> npcList.forEach(npc -> player.send(new NpcInfo(npc))));
        for (L2Player player : onlinePlayers) {
            for (L2Npc npc : npcList) {
                log.info("Notifying player {} on NPC {}", player, npc);
                player.send(new NpcInfo(npc));
            }
        }
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

    public void spawnNpc(L2Npc npcInstance) {
        npcList.add(npcInstance);
        onlinePlayers.forEach(player -> player.send(new NpcInfo(npcInstance)));
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
