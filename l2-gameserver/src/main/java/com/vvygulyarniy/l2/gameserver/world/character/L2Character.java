package com.vvygulyarniy.l2.gameserver.world.character;

import com.vvygulyarniy.l2.domain.geo.Position;
import com.vvygulyarniy.l2.gameserver.world.character.info.CollisionParams;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Phoen-X on 11.03.2017.
 */
@ToString
public abstract class L2Character {
    public static final AtomicInteger ID_SEQUENCE = new AtomicInteger(1);
    @Getter
    protected final int id;
    @Getter
    protected final CollisionParams collisionParams;
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
}
