package com.vvygulyarniy.l2.domain.character;

import com.vvygulyarniy.l2.domain.character.info.CharacterAppearance;
import com.vvygulyarniy.l2.domain.character.profession.Profession;
import com.vvygulyarniy.l2.domain.geo.Position;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class L2Character {
    private final int[][] paperdoll = new int[26][3];
    private int id;
    private String accountName;
    private CharacterAppearance appearance;
    private Profession profession;
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
    @Setter
    @Getter
    private Position position;


    private int runSpeed = 100;
    private int walkSpeed = 50;
    private int swimRunSpeed = 50;
    private int swimWalkSpeed = 25;

    public L2Character(int id, String accountName, Profession profession, CharacterAppearance appearance, String nickName, int level) {
        this.id = id;
        this.accountName = accountName;
        this.appearance = appearance;
        this.profession = profession;
        this.nickName = nickName;
        this.level = level;
    }
}
