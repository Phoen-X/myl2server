package com.vvygulyarniy.l2.gameserver.world.management;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vvygulyarniy.l2.gameserver.world.character.L2Character;
import com.vvygulyarniy.l2.gameserver.world.character.info.stat.Gauge;
import com.vvygulyarniy.l2.gameserver.world.event.CharRegenerated;
import com.vvygulyarniy.l2.gameserver.world.event.DamageReceived;
import com.vvygulyarniy.l2.gameserver.world.event.PlayerEnteredWorldEvent;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Phoen-X on 18.03.2017.
 */
@Slf4j
public class CharRegenerationManager {
    private final EventBus bus;
    private final Map<L2Character, RegenerationContext> regeneratingCharacters = new ConcurrentHashMap<>();
    private final Clock clock;

    public CharRegenerationManager(ScheduledExecutorService scheduler,
                                   EventBus bus,
                                   Clock clock, long tickDelay, TimeUnit timeUnit) {
        this.bus = bus;
        this.bus.register(this);
        this.clock = clock;
        scheduler.scheduleAtFixedRate(this::regenerateTick, 0, 200, timeUnit);
    }

    private static boolean shouldRegenerate(Gauge gauge) {
        return !isFull(gauge) && gauge.isCanRegen();
    }

    private static boolean isFull(Gauge gauge) {
        return gauge.getMaxValue() <= gauge.getCurrValue();
    }

    private void regenerateTick() {
        regeneratingCharacters.forEach((l2Char, ctx) -> regenerate(l2Char, ctx, Instant.now(clock)));
    }

    private void regenerate(L2Character l2Char, RegenerationContext context, Instant now) {
        l2Char.getCp().regen(Duration.between(context.lastRegenerationMade, now).toMillis());
        l2Char.getHp().regen(Duration.between(context.lastRegenerationMade, now).toMillis());
        l2Char.getMp().regen(Duration.between(context.lastRegenerationMade, now).toMillis());
        bus.post(new CharRegenerated(l2Char));
        context.lastRegenerationMade = now;
        if (!shouldRegenerate(l2Char)) {
            regeneratingCharacters.remove(l2Char);
        }
    }

    private boolean shouldRegenerate(L2Character l2Char) {
        return shouldRegenerate(l2Char.getCp()) || shouldRegenerate(l2Char.getHp()) || shouldRegenerate(l2Char.getMp());
    }

    @Subscribe
    public void charReceivedDamage(DamageReceived event) {
        startRegenerationIfNeeded(event.getCharacter());
    }

    @Subscribe
    public void playerEntered(PlayerEnteredWorldEvent event) {
        log.info("Player entered: {}", event.getPlayer());
        startRegenerationIfNeeded(event.getPlayer());
    }

    private void startRegenerationIfNeeded(L2Character dmgTarget) {
        log.info("Checking if should regenerate");
        if (shouldRegenerate(dmgTarget.getCp()) || shouldRegenerate(dmgTarget.getHp()) || shouldRegenerate(dmgTarget.getMp())) {
            log.info("Starting regeneration");
            RegenerationContext ctx = regeneratingCharacters.getOrDefault(dmgTarget,
                                                                          new RegenerationContext(Instant.now(clock)));
            regeneratingCharacters.put(dmgTarget, ctx);
        } else {
            log.info("Regeneration is not needed");
        }
    }

    private static class RegenerationContext {
        private Instant lastRegenerationMade;

        RegenerationContext(Instant lastRegenerationMade) {
            this.lastRegenerationMade = lastRegenerationMade;
        }
    }
}
