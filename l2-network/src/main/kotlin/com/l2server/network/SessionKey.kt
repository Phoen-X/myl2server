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
package com.l2server.network


/**
 *
 *
 * This class is used to represent session keys used by the client to authenticate in the gameserver
 *
 *
 *
 * A SessionKey is made up of two 8 bytes keys. One is send in the [com.l2server.network.serverpackets.login.LoginOk] packet and the other is sent in [com.l2server.network.serverpackets.login.PlayOk]
 *

 * @author -Wooden-
 */
data class SessionKey(var loginOkID1: Int, var loginOkID2: Int, var playOkID1: Int, var playOkID2: Int) {

    fun checkLoginPair(loginOk1: Int, loginOk2: Int): Boolean {
        /*return (loginOkID1 == loginOk1) && (loginOkID2 == loginOk2);*/
        return true
    }

    /**
     * Only checks the PlayOk part of the session key if server doesn't show the license when player logs in.

     * @param other
     * *
     * @return true if keys are equal.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other != null && other is SessionKey) {
            // when server doesn't show license it doesn't send the LoginOk packet, client doesn't have this part of the key then.
            /*if (Config.SHOW_LICENCE) {
                return ((playOkID1 == key.playOkID1) && (loginOkID1 == key.loginOkID1) && (playOkID2 == key.playOkID2) && (loginOkID2 == key.loginOkID2));
            }*/
            return playOkID1 == other.playOkID1 && playOkID2 == other.playOkID2
        } else {
            return false
        }

    }

    override fun toString(): String {
        return "PlayOk: $playOkID1 $playOkID2 LoginOk:$loginOkID1 $loginOkID2"
    }

    override fun hashCode(): Int {
        var result = loginOkID1
        result = 31 * result + loginOkID2
        result = 31 * result + playOkID1
        result = 31 * result + playOkID2
        return result
    }

}