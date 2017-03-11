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

import com.vvygulyarniy.l2.domain.character.L2Character;
import lombok.ToString;

import java.nio.ByteBuffer;

import static com.vvygulyarniy.l2.domain.character.info.stat.BasicStat.*;

@ToString
public class CharSelected extends L2GameServerPacket {
    private final L2Character activeChar;
    private final int sessionId;

    /**
     * @param activeChar
     * @param sessionId
     */
    public CharSelected(L2Character activeChar, int sessionId) {
        this.activeChar = activeChar;
        this.sessionId = sessionId;
    }

    @Override
    protected final void writeImpl(final ByteBuffer buffer) {
        writeC(buffer, 0x0b);

        writeS(buffer, activeChar.getNickName());
        writeD(buffer, activeChar.getId());
        writeS(buffer, ""); // title
        writeD(buffer, sessionId);
        writeD(buffer, activeChar.getClanId());
        writeD(buffer, 0x00); // ??
        writeD(buffer, activeChar.getAppearance().getSex().getId());
        writeD(buffer, activeChar.getClassId().getRace().getId());
        writeD(buffer, activeChar.getClassId().getId());
        writeD(buffer, 0x01); // active ??
        writeD(buffer, activeChar.getPosition().getPoint().getX());
        writeD(buffer, activeChar.getPosition().getPoint().getY());
        writeD(buffer, activeChar.getPosition().getPoint().getZ());

        writeF(buffer, activeChar.getCurrHp());
        writeF(buffer, activeChar.getCurrMp());
        writeD(buffer, activeChar.getSp());
        writeQ(buffer, activeChar.getExp());
        writeD(buffer, activeChar.getLevel());
        writeD(buffer, activeChar.getKarma()); // thx evill33t
        writeD(buffer, activeChar.getPkKills());
        writeD(buffer, activeChar.getClassId().getBasicStatSet().get(INT));
        writeD(buffer, activeChar.getClassId().getBasicStatSet().get(STR));
        writeD(buffer, activeChar.getClassId().getBasicStatSet().get(CON));
        writeD(buffer, activeChar.getClassId().getBasicStatSet().get(MEN));
        writeD(buffer, activeChar.getClassId().getBasicStatSet().get(DEX));
        writeD(buffer, activeChar.getClassId().getBasicStatSet().get(WIT));

        writeD(buffer, 100000 % (24 * 60)); // "reset" on 24th hour
        writeD(buffer, 0x00);

        writeD(buffer, activeChar.getClassId().getId());

        writeD(buffer, 0x00);
        writeD(buffer, 0x00);
        writeD(buffer, 0x00);
        writeD(buffer, 0x00);

        writeB(buffer, new byte[64]);
        writeD(buffer, 0x00);
    }
}
