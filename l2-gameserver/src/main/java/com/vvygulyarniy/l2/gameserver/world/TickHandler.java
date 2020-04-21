package com.vvygulyarniy.l2.gameserver.world;

import com.vvygulyarniy.l2.gameserver.WorldProcessor;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TickHandler {
    private final List<WorldProcessor> worldProcessors;
    private final ScheduledExecutorService scheduler;

    public TickHandler(List<WorldProcessor> worldProcessors,
                       ScheduledExecutorService scheduler,
                       long tickPeriod,
                       TimeUnit timeUnit) {
        this.worldProcessors = worldProcessors;
        this.scheduler = scheduler;

        scheduler.scheduleAtFixedRate(this::processTick, 0, tickPeriod, timeUnit);
    }

    private void processTick() {
        worldProcessors.forEach(WorldProcessor::process);
    }
}
