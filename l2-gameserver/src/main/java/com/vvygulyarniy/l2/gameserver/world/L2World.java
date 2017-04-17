package com.vvygulyarniy.l2.gameserver.world;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vvygulyarniy.l2.gameserver.network.packet.server.AbstractNpcInfo.NpcInfo;
import com.vvygulyarniy.l2.gameserver.network.packet.server.ExQuestItemList;
import com.vvygulyarniy.l2.gameserver.network.packet.server.ItemList;
import com.vvygulyarniy.l2.gameserver.network.packet.server.UserInfo;
import com.vvygulyarniy.l2.gameserver.world.character.L2Npc;
import com.vvygulyarniy.l2.gameserver.world.character.L2Player;
import com.vvygulyarniy.l2.gameserver.world.config.npc.XmlNpcInfoRepository;
import com.vvygulyarniy.l2.gameserver.world.config.npc.XmlNpcSpawnInfoParser;
import com.vvygulyarniy.l2.gameserver.world.event.PlayerEnteredWorldEvent;
import com.vvygulyarniy.l2.gameserver.world.event.npc.NpcSpawned;
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
        this.ticksPerSecond = ticksPerSecond;
        int tickDelay = 1000 / this.ticksPerSecond;
        this.positionManager = new PositionManager(gameTimeProvider,
                                                   eventBus,
                                                   executorService,
                                                   tickDelay,
                                                   MILLISECONDS);
        Path npcInfoFile = Paths.get(ClassLoader.getSystemResource("npc_info.xml").toURI());
        this.spawnManager = new NpcSpawnManager(this.executorService, eventBus,
                                                new XmlNpcInfoRepository(new XmlNpcSpawnInfoParser(npcInfoFile)),
                                                tickDelay, MILLISECONDS
        );
        eventBus.register(this);
    }

    @Subscribe
    public void playerEnter(PlayerEnteredWorldEvent event) {
        L2Player player = event.getPlayer();
        onlinePlayers.add(player);
        player.send(new UserInfo(player));
        player.send(new ItemList(new ArrayList<>(), false));
        player.send(new ExQuestItemList());
    }

    @Subscribe
    public void spawnNpc(NpcSpawned event) {
        L2Npc npc = event.getNpc();
        npcList.add(npc);
        onlinePlayers.stream()
                     .filter(player -> player.getPosition().distanceTo(npc.getPosition()) < 900)
                     .forEach(player -> player.send(new NpcInfo(npc)));
    }


    public List<L2Player> getOnlinePlayers() {
        return new ArrayList<>(onlinePlayers);
    }
}
