package com.l2server.network.serverpackets.game;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by Phoen-X on 23.02.2017.
 */
@Builder
@Getter
@ToString
public final class L2CharData {
    private final int[][] paperdoll = new int[31][3];
    private int objectId = 0;
    private String name;
    private long exp = 0;
    private int sp = 0;
    private int clanId = 0;
    private int race = 0;
    private int classId = 0;
    private int baseClassId = 0;
    private long deleteTimer = 0L;
    private long lastAccess = 0L;
    private int face = 0;
    private int hairStyle = 0;
    private int hairColor = 0;
    private int sex = 0;
    private int level = 1;
    private double maxHp = 0;
    private double currentHp = 0;
    private double maxMp = 0;
    private double currentMp = 0;
    private int karma = 0;
    private int pkKills = 0;
    private int pvpKills = 0;
    private int augmentationId = 0;
    private int x = 0;
    private int y = 0;
    private int z = 0;
    private String htmlPrefix = null;
    private int vitalityPoints = 0;
    private int accessLevel = 0;

}
