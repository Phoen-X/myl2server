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
package com.l2server.network.clientpackets.login


import com.l2server.network.ClientPacketProcessor
import com.l2server.network.login.L2LoginClient

/**
 * Format: ddddd

 * @author -Wooden-
 */
class AuthGameGuard : L2LoginClientPacket() {
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

    override fun process(processor: ClientPacketProcessor, client: L2LoginClient) {
        processor.process(this, client)
    }

    override fun toString(): String {
        return "com.l2server.network.clientpackets.login.AuthGameGuard(_sessionId=" + this.sessionId + ", _data1=" + this.data1 + ", _data2=" + this.data2 + ", _data3=" + this.data3 + ", _data4=" + this.data4 + ")"
    }

    companion object {
        private val log = org.slf4j.LoggerFactory.getLogger(AuthGameGuard::class.java)
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