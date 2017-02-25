package com.vvygulyarniy.l2.domain.character;

import com.vvygulyarniy.l2.domain.character.info.CharacterAppearance;
import com.vvygulyarniy.l2.domain.character.info.ClassId;
import lombok.Getter;

@Getter
public class L2Character {
    private String accountName;
    private CharacterAppearance appearance;
    private ClassId classId;
    private String nickName;
    private int level;

    public L2Character(String accountName, ClassId classId, CharacterAppearance appearance, String nickName, int level) {
        this.accountName = accountName;
        this.appearance = appearance;
        this.classId = classId;
        this.nickName = nickName;
        this.level = level;
    }
}
