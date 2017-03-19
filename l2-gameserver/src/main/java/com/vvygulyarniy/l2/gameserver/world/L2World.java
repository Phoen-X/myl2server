package com.vvygulyarniy.l2.gameserver.world;

import com.google.common.eventbus.EventBus;
import com.vvygulyarniy.l2.gameserver.network.packet.server.AbstractNpcInfo.NpcInfo;
import com.vvygulyarniy.l2.gameserver.network.packet.server.ExQuestItemList;
import com.vvygulyarniy.l2.gameserver.network.packet.server.ItemList;
import com.vvygulyarniy.l2.gameserver.network.packet.server.MoveToLocation;
import com.vvygulyarniy.l2.gameserver.network.packet.server.UserInfo;
import com.vvygulyarniy.l2.gameserver.world.character.L2Npc;
import com.vvygulyarniy.l2.gameserver.world.character.L2Player;
import com.vvygulyarniy.l2.gameserver.world.config.npc.XmlNpcInfoRepository;
import com.vvygulyarniy.l2.gameserver.world.config.npc.XmlNpcSpawnInfoParser;
import com.vvygulyarniy.l2.gameserver.world.event.PlayerEnteredWorldEvent;
import com.vvygulyarniy.l2.gameserver.world.npc.NpcSpawnManager;
import com.vvygulyarniy.l2.gameserver.world.position.PositionManager;
import com.vvygulyarniy.l2.gameserver.world.time.GameTimeProvider;
import lombok.extern.slf4j.Slf4j;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Created by Phoen-X on 03.03.2017.
 */
@Slf4j
public class L2World {
    private final EventBus eventBus;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(4);
    private final PositionManager positionManager;
    private final NpcSpawnManager spawnManager;
    private final int ticksPerSecond;
    private List<L2Player> onlinePlayers = new ArrayList<>();
    private List<L2Npc> npcList = new ArrayList<>();

    public L2World(GameTimeProvider gameTimeProvider,
                   EventBus eventBus,
                   int ticksPerSecond) throws JDOMException, IOException, URISyntaxException {
        this.eventBus = eventBus;
        log.info("Even bus: {}", eventBus);

        this.ticksPerSecond = ticksPerSecond;
        Path npcInfoFile = Paths.get(ClassLoader.getSystemResource("npc_info.xml").toURI());
        this.spawnManager = new NpcSpawnManager(eventBus,
                                                new XmlNpcInfoRepository(new XmlNpcSpawnInfoParser(npcInfoFile)));
        int tickDelay = 1000 / this.ticksPerSecond;
        this.positionManager = new PositionManager(gameTimeProvider,
                                                   eventBus,
                                                   executorService,
                                                   tickDelay,
                                                   MILLISECONDS);
        this.executorService.scheduleAtFixedRate(spawnManager::spawnNpcs, 30000, tickDelay, MILLISECONDS);
    }

    public void enterWorld(L2Player player) {
        onlinePlayers.add(player);
        player.send(new UserInfo(player));
        player.send(new ItemList(new ArrayList<>(), false));
        player.send(new ExQuestItemList());
        player.send(new MoveToLocation(player, player.getPosition()));
        eventBus.post(new PlayerEnteredWorldEvent(player));
    }

    public void spawnNpc(L2Npc npcInstance) {
        npcList.add(npcInstance);
        onlinePlayers.forEach(player -> player.send(new NpcInfo(npcInstance)));
    }


    public List<L2Player> getOnlinePlayers() {
        return new ArrayList<>(onlinePlayers);
    }
}
