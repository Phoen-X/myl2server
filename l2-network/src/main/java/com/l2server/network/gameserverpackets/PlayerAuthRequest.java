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
package com.l2server.network.gameserverpackets;


import com.l2server.network.BaseRecievePacket;

/**
 * @author -Wooden-
 */
public class PlayerAuthRequest extends BaseRecievePacket {
    public PlayerAuthRequest(byte[] decrypt) {
        super(decrypt);
        String account = readS();
        int playKey1 = readD();
        int playKey2 = readD();
        int loginKey1 = readD();
        int loginKey2 = readD();
        /*SessionKey sessionKey = new SessionKey(loginKey1, loginKey2, playKey1, playKey2);

        PlayerAuthResponse authResponse;
        if (Config.DEBUG) {

        }
        SessionKey key = LoginController.getInstance().getKeyForAccount(account);
        if ((key != null) && key.equals(sessionKey)) {
            if (Config.DEBUG) {

            }
            LoginController.getInstance().removeAuthedLoginClient(account);
            authResponse = new PlayerAuthResponse(account, true);
        } else {
            if (Config.DEBUG) {


            }
            authResponse = new PlayerAuthResponse(account, false);
        }
        server.sendPacket(authResponse);*/
    }
}
