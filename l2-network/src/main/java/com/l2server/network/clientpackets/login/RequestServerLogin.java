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
package com.l2server.network.clientpackets.login;


import com.l2server.network.ClientPacketProcessor;
import com.l2server.network.L2LoginClient;
import lombok.ToString;

/**
 * <pre>
 * Format is ddc
 * d: first part of session id
 * d: second part of session id
 * c: server ID
 * </pre>
 */
@ToString
public class RequestServerLogin extends L2LoginClientPacket {
    private int _skey1;
    private int _skey2;
    private int _serverId;

    /**
     * @return
     */
    public int getSessionKey1() {
        return _skey1;
    }

    /**
     * @return
     */
    public int getSessionKey2() {
        return _skey2;
    }

    /**
     * @return
     */
    public int getServerID() {
        return _serverId;
    }

    @Override
    public boolean readImpl() {
        if (super._buf.remaining() >= 9) {
            _skey1 = readD();
            _skey2 = readD();
            _serverId = readC();
            return true;
        }
        return false;
    }

    @Override
    public void process(ClientPacketProcessor processor, L2LoginClient client) {
        processor.process(this, client);
    }

   /* @Override
    public void run() {
        SessionKey sk = getClient().getSessionKey();

        // if we didnt showed the license we cant check these values
        if (sk.checkLoginPair(_skey1, _skey2)) {
            if (LoginController.getInstance().isLoginPossible(getClient(), _serverId)) {
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
