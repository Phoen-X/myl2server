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


import com.vvygulyarniy.l2.domain.geo.Position;
import com.vvygulyarniy.l2.gameserver.world.character.L2Player;
import lombok.ToString;

import java.nio.ByteBuffer;

@ToString
public final class MoveToLocation extends L2GameServerPacket {
    private final int _charObjId, _x, _y, _z, _xDst, _yDst, _zDst;

    public MoveToLocation(L2Player cha, Position newPosition) {
        _charObjId = cha.getId();
        _x = cha.getPosition().getPoint().getX();
        _y = cha.getPosition().getPoint().getY();
        _z = cha.getPosition().getPoint().getZ();
        _xDst = newPosition.getPoint().getX();
        _yDst = newPosition.getPoint().getY();
        _zDst = newPosition.getPoint().getZ();
    }

    @Override
    protected final void writeImpl(ByteBuffer buffer) {
        writeC(buffer, 0x2f);

        writeD(buffer, _charObjId);

        writeD(buffer, _xDst);
        writeD(buffer, _yDst);
        writeD(buffer, _zDst);

        writeD(buffer, _x);
        writeD(buffer, _y);
        writeD(buffer, _z);
    }
}
