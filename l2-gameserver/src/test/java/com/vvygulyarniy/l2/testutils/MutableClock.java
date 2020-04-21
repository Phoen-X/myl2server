package com.vvygulyarniy.l2.testutils;



import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

/**
 * Phoen-X on 18.03.2017.
 */
public class MutableClock extends Clock {
    private Clock clock;

    private MutableClock(Clock clock) {
        this.clock = clock;
    }

    public static MutableClock fromNow() {
        return new MutableClock(Clock.fixed(Instant.now(), ZoneId.systemDefault()));
    }

    public void tick(Duration duration) {
        clock = Clock.fixed(clock.instant().plusMillis(duration.toMillis()), ZoneId.systemDefault());
    }

    @Override
    public ZoneId getZone() {
        return clock.getZone();
    }

    @Override
    public Clock withZone(ZoneId zone) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public Instant instant() {
        return clock.instant();
    }

    @Override
    public long millis() {
        return clock.millis();
    }


}
