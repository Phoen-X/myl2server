package com.vvygulyarniy.l2.gameserver.world.position;

import com.google.common.eventbus.EventBus;
import com.vvygulyarniy.l2.domain.geo.Position;
import com.vvygulyarniy.l2.gameserver.world.character.L2Player;
import com.vvygulyarniy.l2.gameserver.world.character.info.CharacterAppearance;
import com.vvygulyarniy.l2.gameserver.world.character.info.ClassId;
import com.vvygulyarniy.l2.gameserver.world.event.MoveStoppedEvent;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Instant;

import static com.vvygulyarniy.l2.gameserver.world.character.info.CharacterAppearance.Sex.MALE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by Phoen-X on 03.03.2017.
 */
public class PositionManagerTest {

    private PositionManager manager;
    private EventBus eventBus;

    @BeforeMethod
    public void setUp() throws Exception {
        eventBus = mock(EventBus.class);
        manager = new PositionManager(eventBus);
    }

    @Test
    public void shouldHandleMovingStart() throws Exception {
        L2Player l2Char = createTestChar();
        l2Char.setRunSpeed(100);
        l2Char.setPosition(new Position(0, 0, 0));
        l2Char.setMoveTarget(new Position(200, 200, 200));

        manager.startMoving(l2Char, Instant.now());
    }

    @Test
    public void shouldNotMoveTargetIfTimeIsEqualToMoveStartTime() throws Exception {
        L2Player l2Char = createTestChar();
        l2Char.setRunSpeed(100);
        Position startPosition = new Position(0, 0, 0);
        Position targetPosition = new Position(200, 200, 200);

        l2Char.setPosition(startPosition);
        l2Char.setMoveTarget(targetPosition);

        Instant now = Instant.now();
        manager.startMoving(l2Char, now);
        manager.updatePositions(now);

        assertThat(l2Char.getPosition()).isEqualTo(startPosition);
    }

    @Test
    public void shouldMoveToTargetWhenTimeIsEnoughToReachIt() throws Exception {
        L2Player l2Char = createTestChar();
        l2Char.setRunSpeed(100);
        Position startPosition = new Position(0, 0, 0);
        Position targetPosition = new Position(200, 200, 200);

        l2Char.setPosition(startPosition);
        l2Char.setMoveTarget(targetPosition);

        Instant now = Instant.now();
        manager.startMoving(l2Char, now);
        manager.updatePositions(now.plusSeconds(4));

        assertThat(l2Char.getPosition()).isEqualTo(targetPosition);
    }

    @Test
    public void shouldMoveToTargetByStepsEachUpdateIsCalled() throws Exception {
        L2Player l2Char = createTestChar();
        l2Char.setRunSpeed(100);
        Position startPosition = new Position(0, 0, 0);
        Position targetPosition = new Position(0, 300, 0);

        l2Char.setPosition(startPosition);
        l2Char.setMoveTarget(targetPosition);

        Instant now = Instant.now();
        manager.startMoving(l2Char, now);
        manager.updatePositions(now.plusSeconds(1));
        assertThat(l2Char.getPosition()).isEqualTo(new Position(0, 100, 0));
        manager.updatePositions(now.plusSeconds(2));
        assertThat(l2Char.getPosition()).isEqualTo(new Position(0, 200, 0));
        manager.updatePositions(now.plusSeconds(3));
        assertThat(l2Char.getPosition()).isEqualTo(targetPosition);
    }

    @Test
    public void shouldNotifyPlayerWhenMoveFinished() throws Exception {
        L2Player l2Char = createTestChar();
        l2Char.setRunSpeed(49);
        Position startPosition = new Position(0, 0, 0);
        Position targetPosition = new Position(0, 50, 0);

        l2Char.setPosition(startPosition);
        l2Char.setMoveTarget(targetPosition);

        Instant now = Instant.now();
        manager.startMoving(l2Char, now);
        manager.updatePositions(now.plusSeconds(1));
        verify(eventBus, never()).post(new MoveStoppedEvent(l2Char));
        manager.updatePositions(now.plusSeconds(2));
        verify(eventBus, times(1)).post(new MoveStoppedEvent(l2Char));
    }

    private L2Player createTestChar() {
        return new L2Player(1,
                            "test_acc",
                            ClassId.elvenFighter,
                            new CharacterAppearance(MALE, (byte) 0, (byte) 0, (byte) 0),
                            "test_name", 10);
    }
}