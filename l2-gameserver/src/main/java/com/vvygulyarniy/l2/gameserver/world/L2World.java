package com.vvygulyarniy.l2.gameserver.world;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vvygulyarniy.l2.gameserver.network.packet.server.AbstractNpcInfo.NpcInfo;
import com.vvygulyarniy.l2.gameserver.network.packet.server.ExQuestItemList;
import com.vvygulyarniy.l2.gameserver.network.packet.server.ItemList;
import com.vvygulyarniy.l2.gameserver.network.packet.server.UserInfo;
import com.vvygulyarniy.l2.gameserver.world.character.L2Npc;
import com.vvygulyarniy.l2.gameserver.world.character.L2Player;
import com.vvygulyarniy.l2.gameserver.world.event.PlayerEnteredWorldEvent;
import com.vvygulyarniy.l2.gameserver.world.event.npc.NpcSpawned;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
public class L2World {
    private final ReentrantReadWriteLock playersLock = new ReentrantReadWriteLock();
    private final List<L2Player> onlinePlayers = new ArrayList<>();
    private final List<L2Npc> npcList = new ArrayList<>();

    public L2World(EventBus eventBus) {
        eventBus.register(this);
    }

    @Subscribe
    public void playerEntered(PlayerEnteredWorldEvent event) {
        L2Player player = event.getPlayer();
        playersLock.writeLock().lock();
        try {
            onlinePlayers.add(player);
        } finally {
            playersLock.writeLock().unlock();
        }
        player.send(new UserInfo(player));
        player.send(new ItemList(new ArrayList<>(), false));
        player.send(new ExQuestItemList());
    }

    @Subscribe
    public void spawnNpc(NpcSpawned event) {
        L2Npc npc = event.getNpc();
        npcList.add(npc);
        playersLock.readLock().lock();
        try {
            onlinePlayers.stream()
                         .filter(player -> player.getPosition().distanceTo(npc.getPosition()) < 900)
                         .forEach(player -> player.send(new NpcInfo(npc)));
        } finally {
            playersLock.readLock().unlock();
        }
    }

    public List<L2Player> getOnlinePlayers() {
        playersLock.readLock().lock();
        try {
            return new ArrayList<>(onlinePlayers);
        } finally {
            playersLock.readLock().unlock();
        }
    }
}
