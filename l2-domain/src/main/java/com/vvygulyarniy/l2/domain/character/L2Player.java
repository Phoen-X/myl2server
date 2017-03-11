package com.vvygulyarniy.l2.domain.character;

import com.vvygulyarniy.l2.domain.character.gear.PaperDoll;
import com.vvygulyarniy.l2.domain.character.info.CharacterAppearance;
import com.vvygulyarniy.l2.domain.character.info.ClassId;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class L2Player extends L2Character {
    private final PaperDoll paperDoll = new PaperDoll();
    private String accountName;
    private CharacterAppearance appearance;
    private ClassId classId;
    @Getter
    private int exp;
    @Getter
    private int sp;
    @Setter
    @Getter
    private int maxCp;
    @Setter
    @Getter
    private int currCp;
    @Setter
    @Getter
    private int currLoad;
    @Setter
    @Getter
    private int maxLoad;
    @Getter
    private int clanId;
    @Getter
    private int karma;
    @Getter
    private int pkKills;


    public L2Player(int id,
                    String accountName,
                    ClassId classId,
                    CharacterAppearance appearance,
                    String name,
                    int level) {
        super(id, name, level, classId.getCollisionParams(appearance.getSex()));
        this.accountName = accountName;
        this.appearance = appearance;
        this.classId = classId;
    }


}
