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
package com.vvygulyarniy.l2.loginserver.communication.packet.client


import java.nio.ByteBuffer

/**
 * Format: ddddd

 * @author -Wooden-
 */
class AuthGameGuard(buffer: ByteBuffer) : L2LoginClientPacket(buffer) {
    var sessionId: Int = 0
        private set
    var data1: Int = 0
        private set
    var data2: Int = 0
        private set
    var data3: Int = 0
        private set
    var data4: Int = 0
        private set

    override fun readImpl(): Boolean {
        if (super.buffer!!.remaining() >= 20) {
            sessionId = readD()
            data1 = readD()
            data2 = readD()
            data3 = readD()
            data4 = readD()
            return true
        }
        return false
    }

    override fun toString(): String {
        return "AuthGameGuard(sessionId=$sessionId, data1=$data1, data2 $data2, data3=$data3, data4=$data4)"
    }
    /*@Override
    public void run() {
        if (_sessionId == getClient().getSessionId()) {
            getClient().setState(L2LoginClient.LoginClientState.AUTHED_GG);
            getClient().sendPacket(new GGAuth(getClient().getSessionId()));
        } else {
            getClient().close(LoginFail.LoginFailReason.REASON_ACCESS_FAILED);
        }
    }*/
}
