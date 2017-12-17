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
package com.vvygulyarniy.l2.loginserver.communication.packet.server


import com.l2server.network.SessionKey
import java.nio.ByteBuffer

/**
 * <pre>
 * Format: dddddddd
 * f: the session key
 * d: ?
 * d: ?
 * d: ?
 * d: ?
 * d: ?
 * d: ?
 * b: 16 bytes - unknown
</pre> *
 */
data class LoginOk(val sessionKey: SessionKey) : L2LoginServerPacket() {
    private val _loginOk1: Int = sessionKey.loginOkID1
    private val _loginOk2: Int = sessionKey.loginOkID2

    override fun write(buffer: ByteBuffer) {
        writeC(buffer, 0x03)
        writeD(buffer, _loginOk1)
        writeD(buffer, _loginOk2)
        writeD(buffer, 0x00)
        writeD(buffer, 0x00)
        writeD(buffer, 0x000003ea)
        writeD(buffer, 0x00)
        writeD(buffer, 0x00)
        writeD(buffer, 0x00)
        writeB(buffer, ByteArray(16))
    }
}
