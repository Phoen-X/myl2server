package com.vvygulyarniy.l2.gameserver.config;

import com.google.common.eventbus.EventBus;
import com.vvygulyarniy.l2.gameserver.world.L2World;
import com.vvygulyarniy.l2.gameserver.world.event.MoveStoppedEventListener;
import org.jdom2.JDOMException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URISyntaxException;


/**
 * Phoen-X on 16.03.2017.
 */
@Configuration
public class SpringConfig {
    @Bean
    EventBus createEventBus() {
        return new EventBus("game_events");
    }

    @Bean
    public L2World world(EventBus eventBus) throws JDOMException, IOException, URISyntaxException {
        return new L2World(eventBus, 10);
    }

    @Bean
    public MoveStoppedEventListener listener(final EventBus bus) {
        MoveStoppedEventListener listener = new MoveStoppedEventListener();
        bus.register(listener);
        return listener;
    }
}
