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

import com.vvygulyarniy.l2.domain.castle.Castle;
import lombok.ToString;

import java.nio.ByteBuffer;
import java.util.List;

import static java.util.Comparator.comparing;

/**
 * @author l3x
 */
@ToString
public final class ExSendManorList extends L2GameServerPacket {
    private final List<Castle> castleList;

    public ExSendManorList(List<Castle> castleList) {
        this.castleList = castleList;
    }

    @Override
    protected void writeImpl(ByteBuffer buffer) {
        castleList.sort(comparing(Castle::getId));

        writeC(buffer, 0xFE);
        writeH(buffer, 0x22);
        writeD(buffer, castleList.size()); //castles.size()

        for (Castle castle : castleList) {
            writeD(buffer, castle.getId());
            writeS(buffer, castle.getName().toLowerCase());
        }
    }
}