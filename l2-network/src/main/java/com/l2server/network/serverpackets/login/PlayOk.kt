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


import com.l2server.network.SessionKey

import java.nio.ByteBuffer

/**

 */
class PlayOk(sessionKey: SessionKey) : L2LoginServerPacket() {
    private val _playOk1: Int = sessionKey.playOkID1
    private val _playOk2: Int = sessionKey.playOkID2

    override fun write(buffer: ByteBuffer) {
        writeC(buffer, 0x07)
        writeD(buffer, _playOk1)
        writeD(buffer, _playOk2)
    }

    override fun toString(): String {
        return "PlayOk(_playOk1=" + this._playOk1 + ", _playOk2=" + this._playOk2 + ")"
    }
}
