/*
 * Copyright (C) 2004-2015 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2server.network.clientpackets.game;

import com.l2server.network.GameServerPacketProcessor;
import com.l2server.network.L2GameClient;
import lombok.Getter;
import lombok.ToString;

@SuppressWarnings("unused")
@Getter
@ToString
public final class CharacterCreate extends L2GameClientPacket {
    private static final String _C__0C_CHARACTERCREATE = "[C] 0C CharacterCreate";

    // cSdddddddddddd
    private String name;
    private int race;
    private byte sex;
    private int classId;
    private int baseInt;
    private int baseStr;
    private int baseCon;
    private int baseMen;
    private int baseDex;
    private int baseWit;
    private byte hairStyle;
    private byte hairColor;
    private byte face;

    @Override
    protected void readImpl() {
        name = readS();
        race = readD();
        sex = (byte) readD();
        classId = readD();
        baseInt = readD();
        baseStr = readD();
        baseCon = readD();
        baseMen = readD();
        baseDex = readD();
        baseWit = readD();
        hairStyle = (byte) readD();
        hairColor = (byte) readD();
        face = (byte) readD();
    }

    @Override
    public void process(GameServerPacketProcessor processor, L2GameClient client) {
        processor.process(this, client);
    }
}