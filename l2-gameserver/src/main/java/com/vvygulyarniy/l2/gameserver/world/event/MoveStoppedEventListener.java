package com.vvygulyarniy.l2.gameserver.world.event;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vvygulyarniy.l2.gameserver.network.packet.server.StopMove;
import com.vvygulyarniy.l2.gameserver.world.character.L2Player;
import lombok.extern.slf4j.Slf4j;

/**
 * Phoen-X on 17.03.2017.
 */
@Slf4j
public class MoveStoppedEventListener {
    private final EventBus bus;

    public MoveStoppedEventListener(EventBus bus) {
        this.bus = bus;
        bus.register(this);
    }

    @Subscribe
    public void characterStopped(MoveStopped event) {
        log.info("Move stopped: {}", event.getCharacter());
        if (event.getCharacter() instanceof L2Player) {
            L2Player player = (L2Player) event.getCharacter();
            player.send(new StopMove(player.getId(), player.getPosition()));
        }

    }
}
