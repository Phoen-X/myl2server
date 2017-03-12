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

import lombok.ToString;

import java.nio.ByteBuffer;

/**
 * @author KenM
 */
@ToString
public class ExRedSky extends L2GameServerPacket {
    private final int _duration;

    public ExRedSky(int duration) {
        _duration = duration;
    }

    @Override
    protected void writeImpl(final ByteBuffer buffer) {
        writeC(buffer, 0xFE);
        writeH(buffer, 0x41);
        writeD(buffer, _duration);
    }
}
