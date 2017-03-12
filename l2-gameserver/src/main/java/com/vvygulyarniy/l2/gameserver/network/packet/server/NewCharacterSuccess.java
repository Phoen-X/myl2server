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
package com.vvygulyarniy.l2.gameserver.network.packet.server;

import com.vvygulyarniy.l2.gameserver.world.character.profession.Profession;

import java.nio.ByteBuffer;
import java.util.List;

import static com.vvygulyarniy.l2.gameserver.world.character.info.stat.BasicStat.*;

public final class NewCharacterSuccess extends L2GameServerPacket {
    private final List<Profession> professionsAvailable;

    public NewCharacterSuccess(List<Profession> professionsAvailable) {
        this.professionsAvailable = professionsAvailable;
    }


    @Override
    protected void writeImpl(ByteBuffer buffer) {
        writeC(buffer, 0x0D);
        writeD(buffer, professionsAvailable.size());

        for (Profession profession : professionsAvailable) {
            writeD(buffer, profession.getRace().ordinal());
            writeD(buffer, profession.getId());
            writeD(buffer, 0x46);
            writeD(buffer, profession.getStats().get(STR));
            writeD(buffer, 0x0A);
            writeD(buffer, 0x46);
            writeD(buffer, profession.getStats().get(DEX));
            writeD(buffer, 0x0A);
            writeD(buffer, 0x46);
            writeD(buffer, profession.getStats().get(CON));
            writeD(buffer, 0x0A);
            writeD(buffer, 0x46);
            writeD(buffer, profession.getStats().get(INT));
            writeD(buffer, 0x0A);
            writeD(buffer, 0x46);
            writeD(buffer, profession.getStats().get(WIT));
            writeD(buffer, 0x0A);
            writeD(buffer, 0x46);
            writeD(buffer, profession.getStats().get(MEN));
            writeD(buffer, 0x0A);
        }
    }
}
