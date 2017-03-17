package com.vvygulyarniy.l2.gameserver.world.event;

import com.google.common.eventbus.Subscribe;
import com.vvygulyarniy.l2.gameserver.network.packet.server.StopMove;
import com.vvygulyarniy.l2.gameserver.world.character.L2Player;
import lombok.extern.slf4j.Slf4j;

/**
 * Phoen-X on 17.03.2017.
 */
@Slf4j
public class MoveStoppedEventListener {
    @Subscribe
    public void characterStopped(MoveStoppedEvent event) {
        log.info("Move stopped: {}", event.getCharacter());
        if (event.getCharacter() instanceof L2Player) {
            L2Player player = (L2Player) event.getCharacter();
            player.send(new StopMove(player.getId(), player.getPosition()));
        }

    }
}
