package com.vvygulyarniy.l2.domain.character;

import com.vvygulyarniy.l2.domain.character.event.MoveStoppedEventListener;
import com.vvygulyarniy.l2.domain.character.event.MoveStoppedEventListener.MoveStoppedEvent;
import com.vvygulyarniy.l2.domain.character.gear.PaperDoll;
import com.vvygulyarniy.l2.domain.character.info.CharacterAppearance;
import com.vvygulyarniy.l2.domain.character.info.ClassId;
import com.vvygulyarniy.l2.domain.character.info.CollisionParams;
import com.vvygulyarniy.l2.domain.geo.Position;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@ToString
public class L2Character {
    private final CollisionParams collisionParams;
    private final PaperDoll paperDoll = new PaperDoll();
    private final Collection<MoveStoppedEventListener> moveStoppedListeners = new ArrayList<>();
    private int id;
    private String accountName;
    private CharacterAppearance appearance;
    private ClassId classId;
    private String nickName;
    private int level;
    private int exp;
    private int sp;
    @Setter
    private int maxCp;
    @Setter
    private int currCp;
    @Setter
    private int maxHp;
    @Setter
    private int currHp;
    @Setter
    private int maxMp;
    @Setter
    private int currMp;
    @Setter
    private int currLoad;
    @Setter
    private int maxLoad;
    private int clanId;
    private int karma;
    private int pkKills;
    @Setter
    private Position position;
    private Position moveTarget;
    @Setter
    private double runSpeed = 100;
    private int walkSpeed = 50;
    private int swimRunSpeed = 50;
    private int swimWalkSpeed = 25;


    public L2Character(int id,
                       String accountName,
                       ClassId classId,
                       CharacterAppearance appearance,
                       String nickName,
                       int level) {
        this.id = id;
        this.accountName = accountName;
        this.appearance = appearance;
        this.classId = classId;
        this.collisionParams = classId.getCollisionParams(appearance.getSex());
        this.nickName = nickName;
        this.level = level;
    }

    public void setMoveTarget(Position moveTo) {
        this.moveTarget = moveTo;
    }

    public void moveStopped() {
        MoveStoppedEvent event = new MoveStoppedEvent(this, position);
        moveTarget = null;
        moveStoppedListeners.forEach(listener -> listener.movingStopped(event));
    }


    public void listenEvent(MoveStoppedEventListener moveStoppedEventListener) {
        this.moveStoppedListeners.add(moveStoppedEventListener);
    }
}
