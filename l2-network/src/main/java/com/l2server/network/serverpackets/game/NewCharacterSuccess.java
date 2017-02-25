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

import com.vvygulyarniy.l2.domain.character.info.L2CharTemplate;

import java.nio.ByteBuffer;
import java.util.List;

public final class NewCharacterSuccess extends L2GameServerPacket {
    private final List<L2CharTemplate> templatesAvailable;

    public NewCharacterSuccess(List<L2CharTemplate> templatesAvailable) {
        this.templatesAvailable = templatesAvailable;
    }


    @Override
    protected void writeImpl(ByteBuffer buffer) {
        writeC(buffer, 0x0D);
        writeD(buffer, templatesAvailable.size());

        for (L2CharTemplate template : templatesAvailable) {
            writeD(buffer, template.getRace().ordinal());
            writeD(buffer, template.getClassId().getId());
            writeD(buffer, 0x46);
            writeD(buffer, template.getBaseStr());
            writeD(buffer, 0x0A);
            writeD(buffer, 0x46);
            writeD(buffer, template.getBaseDex());
            writeD(buffer, 0x0A);
            writeD(buffer, 0x46);
            writeD(buffer, template.getBaseCon());
            writeD(buffer, 0x0A);
            writeD(buffer, 0x46);
            writeD(buffer, template.getBaseInt());
            writeD(buffer, 0x0A);
            writeD(buffer, 0x46);
            writeD(buffer, template.getBaseWit());
            writeD(buffer, 0x0A);
            writeD(buffer, 0x46);
            writeD(buffer, template.getBaseMen());
            writeD(buffer, 0x0A);
        }
    }
}
