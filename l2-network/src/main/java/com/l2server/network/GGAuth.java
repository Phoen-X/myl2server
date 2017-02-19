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
package com.l2server.network;


import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

/**
 * Fromat: d d: response
 */
@Slf4j
public final class GGAuth extends L2LoginServerPacket {
    public static final int SKIP_GG_AUTH_REQUEST = 0x0b;
    static final Logger _log = Logger.getLogger(GGAuth.class.getName());
    private final int _response;

    public GGAuth(int response) {
        _response = response;
    }

    @Override
    public void write(ByteBuffer buffer) {
        writeC(buffer, 0x0b);
        writeD(buffer, _response);
        writeD(buffer, 0x00);
        writeD(buffer, 0x00);
        writeD(buffer, 0x00);
        writeD(buffer, 0x00);
    }
}
