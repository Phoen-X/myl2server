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
 * <pre>
 * Format: ddc
 * d: fist part of session id
 * d: second part of session id
 * c: ?
</pre> *
 */
class RequestServerList : L2LoginClientPacket() {
    /**
     * @return
     */
    var sessionKey1: Int = 0
        private set
    /**
     * @return
     */
    var sessionKey2: Int = 0
        private set
    /**
     * @return
     */
    val data3: Int = 0

    public override fun readImpl(): Boolean {
        if (super.buffer!!.remaining() >= 8) {
            sessionKey1 = readD() // loginOk 1
            sessionKey2 = readD() // loginOk 2
            return true
        }
        return false
    }

    override fun process(processor: ClientPacketProcessor, client: L2LoginClient) {
        processor.process(this, client)
    }

    /*@Override
    public void run() {
        if (getClient().getSessionKey().checkLoginPair(_skey1, _skey2)) {
            getClient().sendPacket(new ServerList(getClient()));
        } else {
            getClient().close(LoginFail.LoginFailReason.REASON_ACCESS_FAILED);
        }
    }*/
}
