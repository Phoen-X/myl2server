package com.vvygulyarniy.l2.gameserver.world.time;

import java.time.Instant;

/**
 * Phoen-X on 19.03.2017.
 */
public interface GameTimeProvider {
    Instant now();
}
