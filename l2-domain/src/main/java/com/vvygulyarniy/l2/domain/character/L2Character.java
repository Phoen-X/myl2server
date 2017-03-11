package com.vvygulyarniy.l2.domain.character;

import com.vvygulyarniy.l2.domain.character.event.MoveStoppedEventListener;
import com.vvygulyarniy.l2.domain.character.info.CollisionParams;
import com.vvygulyarniy.l2.domain.geo.Position;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Phoen-X on 11.03.2017.
 */
public abstract class L2Character {
    @Getter
    protected final int id;
    @Getter
    protected final CollisionParams collisionParams;
    protected final Collection<MoveStoppedEventListener> moveStoppedListeners = new ArrayList<>();
    @Getter
    protected String name;
    @Getter
    protected int level;
    @Setter
    @Getter
    protected int maxHp;
    @Setter
    @Getter
    protected int currHp;
    @Setter
    @Getter
    protected int maxMp;
    @Setter
    @Getter
    protected int currMp;
    @Setter
    @Getter
    protected Position position;
    @Getter
    protected Position moveTarget;
    @Setter
    @Getter
    protected double runSpeed = 100;
    @Getter
    protected int walkSpeed = 50;
    @Getter
    protected int swimRunSpeed = 50;
    @Getter
    protected int swimWalkSpeed = 25;

    public L2Character(int id, String name, int level, CollisionParams collisionParams) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.collisionParams = collisionParams;
    }

    public void setMoveTarget(Position moveTo) {
        this.moveTarget = moveTo;
    }

    public void moveStopped() {
        MoveStoppedEventListener.MoveStoppedEvent event = new MoveStoppedEventListener.MoveStoppedEvent(this, position);
        moveTarget = null;
        moveStoppedListeners.forEach(listener -> listener.movingStopped(event));
    }

    public void listenEvent(MoveStoppedEventListener moveStoppedEventListener) {
        this.moveStoppedListeners.add(moveStoppedEventListener);
    }
}
