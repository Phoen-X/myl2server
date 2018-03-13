package com.vvygulyarniy.l2.gameserver.world.position;

import com.google.common.eventbus.EventBus;
import com.vvygulyarniy.l2.domain.geo.Position;
import com.vvygulyarniy.l2.gameserver.domain.ClassId;
import com.vvygulyarniy.l2.gameserver.world.character.L2Player;
import com.vvygulyarniy.l2.gameserver.world.character.info.CharacterAppearance;
import com.vvygulyarniy.l2.gameserver.world.event.MoveRequested;
import com.vvygulyarniy.l2.gameserver.world.event.MoveStopped;
import com.vvygulyarniy.l2.testutils.MutableGameTimeProvider;
import com.vvygulyarniy.l2.testutils.OnDemandScheduledExecutorService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.vvygulyarniy.l2.gameserver.world.character.info.CharacterAppearance.Sex.MALE;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class PositionManagerTest {
    private EventBus eventBus;
    private OnDemandScheduledExecutorService scheduler;
    private MutableGameTimeProvider gameTime;

    @BeforeMethod
    public void setUp() throws Exception {
        gameTime = new MutableGameTimeProvider();
        scheduler = new OnDemandScheduledExecutorService();
        eventBus = new EventBus("main");
        new PositionManager(gameTime, eventBus, scheduler, 10, MILLISECONDS);
    }

    @Test
    public void shouldHandleMovingStart() throws Exception {
        L2Player l2Char = createTestChar();
        l2Char.setRunSpeed(100);
        l2Char.setPosition(new Position(0, 0, 0));
        Position targetPos = new Position(200, 200, 200);

        eventBus.post(new MoveRequested(l2Char, targetPos));

        assertThat(l2Char.getMoveTarget()).isEqualTo(targetPos);
    }

    @Test
    public void shouldNotMoveTargetIfTimeIsEqualToMoveStartTime() throws Exception {
        L2Player l2Char = createTestChar();
        l2Char.setRunSpeed(100);
        Position startPosition = new Position(0, 0, 0);
        Position targetPosition = new Position(200, 200, 200);

        l2Char.setPosition(startPosition);

        eventBus.post(new MoveRequested(l2Char, targetPosition));
        scheduler.runScheduledTasks();

        assertThat(l2Char.getPosition()).isEqualTo(startPosition);
    }

    @Test
    public void shouldMoveToTargetWhenTimeIsEnoughToReachIt() throws Exception {
        L2Player l2Char = createTestChar();
        l2Char.setRunSpeed(100);
        Position startPosition = new Position(0, 0, 0);
        Position targetPosition = new Position(200, 200, 200);

        l2Char.setPosition(startPosition);

        eventBus.post(new MoveRequested(l2Char, targetPosition));

        gameTime.tick(Duration.ofSeconds(4));
        scheduler.runScheduledTasks();

        assertThat(l2Char.getPosition()).isEqualTo(targetPosition);
    }

    @Test
    public void shouldMoveToTargetByStepsEachUpdateIsCalled() throws Exception {
        L2Player l2Char = createTestChar();
        l2Char.setRunSpeed(100);
        Position startPosition = new Position(0, 0, 0);
        Position targetPosition = new Position(0, 300, 0);

        l2Char.setPosition(startPosition);

        eventBus.post(new MoveRequested(l2Char, targetPosition));

        gameTime.tick(Duration.ofSeconds(1));
        scheduler.runScheduledTasks();

        assertThat(l2Char.getPosition()).isEqualTo(new Position(0, 100, 0));

        gameTime.tick(Duration.ofSeconds(1));
        scheduler.runScheduledTasks();

        assertThat(l2Char.getPosition()).isEqualTo(new Position(0, 200, 0));

        gameTime.tick(Duration.ofSeconds(1));
        scheduler.runScheduledTasks();

        assertThat(l2Char.getPosition()).isEqualTo(targetPosition);
    }

    @Test
    public void shouldNotifyWhenMoveFinished() throws Exception {
        L2Player l2Char = createTestChar();
        l2Char.setRunSpeed(49);
        Position startPosition = new Position(0, 0, 0);
        Position targetPosition = new Position(0, 50, 0);

        l2Char.setPosition(startPosition);

        EventBus busMock = mock(EventBus.class);
        PositionManager manager = new PositionManager(gameTime, busMock, scheduler, 1, MILLISECONDS);

        // we imitate event occurrence as we have mock for EventBus
        manager.startMoving(new MoveRequested(l2Char, targetPosition));

        gameTime.tick(Duration.ofSeconds(1));
        scheduler.runScheduledTasks();

        verify(busMock, never()).post(new MoveStopped(l2Char));

        gameTime.tick(Duration.ofSeconds(1));
        scheduler.runScheduledTasks();

        verify(busMock, times(1)).post(new MoveStopped(l2Char));
    }

    private L2Player createTestChar() {
        return new L2Player(1,
                            "test_acc",
                            ClassId.elvenFighter,
                            new CharacterAppearance(MALE, (byte) 0, (byte) 0, (byte) 0),
                            "test_name", 10);
    }
}