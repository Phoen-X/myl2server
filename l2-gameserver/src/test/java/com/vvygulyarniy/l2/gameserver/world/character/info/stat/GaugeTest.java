package com.vvygulyarniy.l2.gameserver.world.character.info.stat;

import org.testng.annotations.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Phoen-X on 18.03.2017.
 */
public class GaugeTest {
    @Test
    public void shouldCreateGaugeWithDefaultValues() throws Exception {
        Gauge gauge = new Gauge(0, 100, 1);
        assertThat(gauge).isNotNull();
    }

    @Test
    public void shouldRegenValueAccordingToCurrentRegenSpeed() throws Exception {
        Gauge gauge = new Gauge(0, 100, 15);
        gauge.regen(Duration.ofSeconds(1).toMillis());
        assertThat(gauge.getCurrValue()).isEqualTo(15);
    }

    @Test
    public void shouldNotIncreaseMoreThanMaxValue() throws Exception {
        Gauge gauge = new Gauge(0, 100, 10);
        gauge.regen(Duration.ofSeconds(11).toMillis());
        assertThat(gauge.getCurrValue()).isEqualTo(100);
    }

    @Test
    public void shouldNotIncreaseWhenAlreadyAtTheMax() throws Exception {
        Gauge gauge = new Gauge(100, 100, 10);
        gauge.regen(Duration.ofSeconds(100).toMillis());
        assertThat(gauge.getCurrValue()).isEqualTo(100);
    }

    @Test
    public void shouldNotIncreaseWhileRegenIsTurnedOff() throws Exception {
        Gauge gauge = new Gauge(0, 20, 10);
        gauge.setCanRegen(false);
        gauge.regen(Duration.ofSeconds(100).toMillis());
        assertThat(gauge.getCurrValue()).isEqualTo(0);

    }
}