package com.vvygulyarniy.l2.gameserver.world.management;

import com.google.common.eventbus.EventBus;
import com.vvygulyarniy.l2.gameserver.world.character.L2Player;
import com.vvygulyarniy.l2.gameserver.world.character.info.CharacterAppearance;
import com.vvygulyarniy.l2.gameserver.world.character.info.ClassId;
import com.vvygulyarniy.l2.gameserver.world.character.info.stat.Gauge;
import com.vvygulyarniy.l2.gameserver.world.event.PlayerEnteredWorldEvent;
import com.vvygulyarniy.l2.testutils.MutableClock;
import com.vvygulyarniy.l2.testutils.OnDemandScheduledExecutorService;
import org.testng.annotations.Test;

import java.time.Clock;
import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;

import static com.vvygulyarniy.l2.gameserver.world.character.info.CharacterAppearance.Sex.MALE;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Phoen-X on 18.03.2017.
 */
public class CharRegenerationManagerTest {
    private static final L2Player TEST_PLAYER = new L2Player(1,
                                                             "some_acc",
                                                             ClassId.abyssWalker,
                                                             new CharacterAppearance(MALE,
                                                                                     (byte) 1,
                                                                                     (byte) 1,
                                                                                     (byte) 1),
                                                             "name", 1);
    private EventBus bus = new EventBus();
    private OnDemandScheduledExecutorService scheduler = new OnDemandScheduledExecutorService();
    private MutableClock fixedClock = MutableClock.fromNow();

    CharRegenerationManager manager = new CharRegenerationManager(scheduler, bus, fixedClock, 50, MILLISECONDS);

    @Test
    public void shouldScheduleRegenerationTaskToSchedulerOnManagerInitialization() throws Exception {
        ScheduledExecutorService scheduler = mock(ScheduledExecutorService.class);
        new CharRegenerationManager(scheduler, bus, Clock.systemUTC(), 1, MILLISECONDS);

        verify(scheduler, times(1)).scheduleAtFixedRate(any(), eq(0L), eq(1L), eq(MILLISECONDS));
    }

    @Test
    public void shouldRegenerateCpOnEveryTick() throws Exception {

        fillGauge(TEST_PLAYER.getCp(), new Gauge(1, 100, 10));

        bus.post(new PlayerEnteredWorldEvent(TEST_PLAYER));
        fixedClock.tick(Duration.ofSeconds(1));
        scheduler.runScheduledTasks();

        assertThat(TEST_PLAYER.getCp().getCurrValue()).isEqualTo(11);
    }

    @Test
    public void shouldRegenerateHpOnEveryTick() throws Exception {
        fillGauge(TEST_PLAYER.getHp(), new Gauge(1, 100, 10));

        bus.post(new PlayerEnteredWorldEvent(TEST_PLAYER));
        fixedClock.tick(Duration.ofSeconds(1));
        scheduler.runScheduledTasks();

        assertThat(TEST_PLAYER.getHp().getCurrValue()).isEqualTo(11);
    }

    @Test
    public void shouldRegenerateMpOnEveryTick() throws Exception {
        fillGauge(TEST_PLAYER.getMp(), new Gauge(1, 100, 10));


        bus.post(new PlayerEnteredWorldEvent(TEST_PLAYER));
        fixedClock.tick(Duration.ofSeconds(1));
        scheduler.runScheduledTasks();

        assertThat(TEST_PLAYER.getMp().getCurrValue()).isEqualTo(11);
    }

    @Test
    public void shouldNotRegenerateCpWhenIsMaximum() throws Exception {
        fillGauge(TEST_PLAYER.getCp(), new Gauge(100, 100, 10));

        bus.post(new PlayerEnteredWorldEvent(TEST_PLAYER));
        fixedClock.tick(Duration.ofSeconds(1));
        scheduler.runScheduledTasks();

        assertThat(TEST_PLAYER.getCp().getCurrValue()).isEqualTo(100);
    }

    @Test
    public void shouldNotRegenerateHealthWhenIsMaximum() throws Exception {
        fillGauge(TEST_PLAYER.getHp(), new Gauge(100, 100, 10));

        bus.post(new PlayerEnteredWorldEvent(TEST_PLAYER));
        fixedClock.tick(Duration.ofSeconds(1));
        scheduler.runScheduledTasks();

        assertThat(TEST_PLAYER.getHp().getCurrValue()).isEqualTo(100);
    }

    @Test
    public void shouldNotRegenerateManaWhenIsMaximum() throws Exception {
        fillGauge(TEST_PLAYER.getMp(), new Gauge(100, 100, 10));

        bus.post(new PlayerEnteredWorldEvent(TEST_PLAYER));
        fixedClock.tick(Duration.ofSeconds(1));
        scheduler.runScheduledTasks();

        assertThat(TEST_PLAYER.getMp().getCurrValue()).isEqualTo(100);
    }

    @Test
    public void shouldNotRegenerateOtherGaugesWhenOnlyOneNeedsRegeneration() throws Exception {
        fillGauge(TEST_PLAYER.getCp(), new Gauge(0, 100, 10));
        fillGauge(TEST_PLAYER.getHp(), new Gauge(100, 100, 10));
        fillGauge(TEST_PLAYER.getMp(), new Gauge(200, 200, 10));

        bus.post(new PlayerEnteredWorldEvent(TEST_PLAYER));
        fixedClock.tick(Duration.ofSeconds(2));
        scheduler.runScheduledTasks();


        assertThat(TEST_PLAYER.getCp().getCurrValue()).isEqualTo(20);
        assertThat(TEST_PLAYER.getHp().getCurrValue()).isEqualTo(100);
        assertThat(TEST_PLAYER.getMp().getCurrValue()).isEqualTo(200);

    }

    private void fillGauge(Gauge gaugeToChange, Gauge valuesToSubstitute) {
        gaugeToChange.setCurrValue(valuesToSubstitute.getCurrValue());
        gaugeToChange.setMaxValue(valuesToSubstitute.getMaxValue());
        gaugeToChange.setRegenPerSecond(valuesToSubstitute.getRegenPerSecond());
    }
}