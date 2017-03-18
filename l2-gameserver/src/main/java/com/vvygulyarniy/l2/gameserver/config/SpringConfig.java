package com.vvygulyarniy.l2.gameserver.config;

import com.google.common.eventbus.EventBus;
import com.vvygulyarniy.l2.gameserver.world.L2World;
import com.vvygulyarniy.l2.gameserver.world.event.CharRegeneratedNotificator;
import com.vvygulyarniy.l2.gameserver.world.event.MoveStoppedEventListener;
import com.vvygulyarniy.l2.gameserver.world.event.npc.NpcSpawnedNotificator;
import com.vvygulyarniy.l2.gameserver.world.management.CharRegenerationManager;
import org.jdom2.JDOMException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


/**
 * Phoen-X on 16.03.2017.
 */
@Configuration
public class SpringConfig {
    @Bean
    public ScheduledExecutorService scheduler() {
        return Executors.newScheduledThreadPool(4);
    }

    @Bean
    EventBus createEventBus() {
        return new EventBus("game_events");
    }

    @Bean
    public L2World world(EventBus eventBus) throws JDOMException, IOException, URISyntaxException {
        return new L2World(eventBus, 50);
    }

    @Bean
    public CharRegenerationManager regenManager(EventBus bus, ScheduledExecutorService scheduler) {
        return new CharRegenerationManager(scheduler, bus, 10);
    }

    @Bean
    public MoveStoppedEventListener moveStoppedListener(final EventBus bus) {
        return new MoveStoppedEventListener(bus);
    }

    @Bean
    public NpcSpawnedNotificator npcSpawnedNotificator(L2World world, EventBus bus) {
        return new NpcSpawnedNotificator(world, bus);
    }

    @Bean
    public CharRegeneratedNotificator regenerationNotificator(L2World world, EventBus bus) {
        return new CharRegeneratedNotificator(world, bus);
    }
}
