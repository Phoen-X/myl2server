package com.vvygulyarniy.l2.gameserver.world.time;

import java.time.Clock;
import java.time.Instant;

/**
 * Phoen-X on 19.03.2017.
 */
public final class CommonClockGameTimeProvider implements GameTimeProvider {
    private final Clock clock;

    private CommonClockGameTimeProvider(Clock clock) {
        this.clock = clock;
    }

    public static CommonClockGameTimeProvider withClock(Clock clock) {
        return new CommonClockGameTimeProvider(clock);
    }

    @Override
    public Instant now() {
        return Instant.now(clock);
    }
}
