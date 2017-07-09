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
package com.l2server.network.serverpackets.login


import java.nio.ByteBuffer

/**
 * Fromat: d d: response
 */
class GGAuth(private val response: Int) : L2LoginServerPacket() {

    override fun write(buffer: ByteBuffer) {
        writeC(buffer, 0x0b)
        writeD(buffer, response)
        writeD(buffer, 0x00)
        writeD(buffer, 0x00)
        writeD(buffer, 0x00)
        writeD(buffer, 0x00)
    }

    override fun toString(): String {
        return "GGAuth(response=" + this.response + ")"
    }

}
