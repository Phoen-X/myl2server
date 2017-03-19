package com.vvygulyarniy.l2.testutils;

import com.vvygulyarniy.l2.gameserver.world.time.GameTimeProvider;

import java.time.Duration;
import java.time.Instant;

/**
 * Phoen-X on 19.03.2017.
 */
public class MutableGameTimeProvider implements GameTimeProvider {
    private MutableClock clock;

    public MutableGameTimeProvider() {
        this.clock = MutableClock.fromNow();
    }

    public void tick(Duration duration) {
        clock.tick(duration);
    }

    @Override
    public Instant now() {
        return Instant.now(clock);
    }
}
