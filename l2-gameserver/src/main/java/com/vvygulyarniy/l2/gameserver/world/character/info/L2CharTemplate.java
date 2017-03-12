package com.vvygulyarniy.l2.gameserver.world.character.info;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public final class L2CharTemplate {
    private final ClassId classId;
    private final int baseCon;
    private final int baseStr;
    private final int baseInt;
    private final int baseDex;
    private final int baseWit;
    private final int baseMen;

}
