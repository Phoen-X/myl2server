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
package com.l2server.network.serverpackets.game;

import lombok.ToString;

import java.nio.ByteBuffer;

@ToString
public class CharSelected extends L2GameServerPacket {
    private final L2CharData activeChar;
    private final int sessionId;

    /**
     * @param activeChar
     * @param sessionId
     */
    public CharSelected(L2CharData activeChar, int sessionId) {
        this.activeChar = activeChar;
        this.sessionId = sessionId;
    }

    @Override
    protected final void writeImpl(final ByteBuffer buffer) {
        writeC(buffer, 0x0b);

        writeS(buffer, activeChar.getName());
        writeD(buffer, activeChar.getObjectId());
        writeS(buffer, ""); // title
        writeD(buffer, sessionId);
        writeD(buffer, activeChar.getClanId());
        writeD(buffer, 0x00); // ??
        writeD(buffer, activeChar.getSex());
        writeD(buffer, activeChar.getRace());
        writeD(buffer, activeChar.getClassId());
        writeD(buffer, 0x01); // active ??
        writeD(buffer, activeChar.getX());
        writeD(buffer, activeChar.getY());
        writeD(buffer, activeChar.getZ());

        writeF(buffer, activeChar.getCurrentHp());
        writeF(buffer, activeChar.getCurrentMp());
        writeD(buffer, activeChar.getSp());
        writeQ(buffer, activeChar.getExp());
        writeD(buffer, activeChar.getLevel());
        writeD(buffer, activeChar.getKarma()); // thx evill33t
        writeD(buffer, activeChar.getPkKills());
        writeD(buffer, activeChar.getStatInt());
        writeD(buffer, activeChar.getStatStr());
        writeD(buffer, activeChar.getStatCon());
        writeD(buffer, activeChar.getStatMen());
        writeD(buffer, activeChar.getStatDex());
        writeD(buffer, activeChar.getStatWit());

        writeD(buffer, 100000 % (24 * 60)); // "reset" on 24th hour
        writeD(buffer, 0x00);

        writeD(buffer, activeChar.getClassId());

        writeD(buffer, 0x00);
        writeD(buffer, 0x00);
        writeD(buffer, 0x00);
        writeD(buffer, 0x00);

        writeB(buffer, new byte[64]);
        writeD(buffer, 0x00);
    }
}
