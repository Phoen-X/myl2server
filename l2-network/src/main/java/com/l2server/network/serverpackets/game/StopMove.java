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

import com.vvygulyarniy.l2.domain.geo.Position;
import lombok.ToString;

import java.nio.ByteBuffer;

@ToString
public final class StopMove extends L2GameServerPacket {
    private final int objectId;
    private final Position position;

    public StopMove(int characterId, Position position) {
        this.objectId = characterId;
        this.position = position;
    }

    @Override
    protected final void writeImpl(final ByteBuffer buffer) {
        writeC(buffer, 0x47);
        writeD(buffer, objectId);
        writeD(buffer, position.getPoint().getX());
        writeD(buffer, position.getPoint().getY());
        writeD(buffer, position.getPoint().getZ());
        writeD(buffer, position.getHeading());
    }
}
