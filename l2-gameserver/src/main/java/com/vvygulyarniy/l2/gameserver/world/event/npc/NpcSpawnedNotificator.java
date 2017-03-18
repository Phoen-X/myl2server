package com.vvygulyarniy.l2.gameserver.world.event.npc;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vvygulyarniy.l2.gameserver.network.packet.server.AbstractNpcInfo;
import com.vvygulyarniy.l2.gameserver.world.L2World;

/**
 * Phoen-X on 18.03.2017.
 */
public class NpcSpawnedNotificator {
    private final L2World world;
    private final EventBus bus;

    public NpcSpawnedNotificator(L2World world, EventBus bus) {
        this.world = world;
        this.bus = bus;
        bus.register(this);
    }

    @Subscribe
    public void npcSpawned(final NpcSpawned event) {
        world.getOnlinePlayers().forEach(plyer -> plyer.send(new AbstractNpcInfo.NpcInfo(event.getNpc())));
    }
}
