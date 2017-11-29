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
package com.vvygulyarniy.l2.loginserver.netty.packet.client


import com.vvygulyarniy.l2.loginserver.netty.login.ClientPacketProcessor
import com.vvygulyarniy.l2.loginserver.netty.login.L2LoginClient

/**
 * <pre>
 * Format is ddc
 * d: first part of session id
 * d: second part of session id
 * c: server ID
</pre> *
 */
class RequestServerLogin : L2LoginClientPacket() {
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
    var serverID: Int = 0
        private set

    public override fun readImpl(): Boolean {
        if (super.buffer!!.remaining() >= 9) {
            sessionKey1 = readD()
            sessionKey2 = readD()
            serverID = readC()
            return true
        }
        return false
    }

    override fun process(processor: ClientPacketProcessor, client: L2LoginClient) {
        processor.process(this, client)
    }

    override fun toString(): String {
        return "com.vvygulyarniy.l2.loginserver.netty.packet.client.RequestServerLogin(_skey1=" + this.sessionKey1 + ", _skey2=" + this.sessionKey2 + ", serverId=" + this.serverID + ")"
    }

    /* @Override
    public void run() {
        SessionKey sk = getClient().getSessionKey();

        // if we didnt showed the license we cant check these values
        if (sk.checkLoginPair(_skey1, _skey2)) {
            if (LoginController.getInstance().isLoginPossible(getClient(), serverId)) {
                getClient().setJoinedGS(true);
                getClient().sendPacket(new PlayOk(sk));
            } else {
                getClient().close(PlayFail.PlayFailReason.REASON_SERVER_OVERLOADED);
            }
        } else {
            getClient().close(LoginFail.LoginFailReason.REASON_ACCESS_FAILED);
        }
    }*/
}
