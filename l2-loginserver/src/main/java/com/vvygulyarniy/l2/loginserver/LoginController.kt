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
package com.vvygulyarniy.l2.loginserver

import com.l2server.network.SessionKey
import com.vvygulyarniy.l2.loginserver.model.data.AccountInfo
import com.vvygulyarniy.l2.loginserver.netty.login.L2LoginClient
import com.vvygulyarniy.l2.loginserver.netty.packet.server.LoginFail.LoginFailReason
import com.vvygulyarniy.l2.loginserver.util.Rnd
import java.net.InetAddress
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object LoginController {
    private val LOGIN_TIMEOUT = 60 * 1000
    private val BLOWFISH_KEYS = 20

    private val clientsConnected = ConcurrentHashMap<String, L2LoginClient>()

    init {

        val purge = PurgeThread()
        purge.isDaemon = true
        purge.start()
    }



    fun assignSessionKeyToClient(account: String, client: L2LoginClient): SessionKey {
        val key: SessionKey

        key = SessionKey(Rnd.nextInt(), Rnd.nextInt(), Rnd.nextInt(), Rnd.nextInt())
        clientsConnected.put(account, client)
        return key
    }

    fun removeAuthedLoginClient(account: String?) {
        if (account == null) {
            return
        }
        clientsConnected.remove(account)
    }

    fun getAuthedClient(account: String): L2LoginClient? {
        return clientsConnected[account]
    }

    fun retriveAccountInfo(login: String, password: String): AccountInfo? {
        try {
            val md = MessageDigest.getInstance("SHA")
            val raw = password.toByteArray(StandardCharsets.UTF_8)
            Base64.getEncoder().encodeToString(md.digest(raw))
            return AccountInfo(login, password, 1, 0)
        } catch (e: Exception) {
            return null
        }
    }

    fun tryCheckinAccount(client: L2LoginClient, info: AccountInfo): AuthLoginResult {
        return AuthLoginResult.AUTH_SUCCESS
    }

    fun getKeyForAccount(account: String): SessionKey? {
        val client = clientsConnected[account]
        if (client != null) {
            return client.sessionKey
        }
        return null
    }


    fun isLoginPossible(): Boolean {
        return true
    }

    /**
     * @param client  the client
     * *
     * @param address client host address
     * *
     * @param info    the account info to checkin
     * *
     * @return true when ok to checkin, false otherwise
     */
    fun canCheckin(client: L2LoginClient, address: InetAddress, info: AccountInfo): Boolean {
        return true
    }

    fun isValidIPAddress(ipAddress: String): Boolean {
        val parts = ipAddress.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (parts.size != 4) {
            return false
        }

        for (s in parts) {
            val i = Integer.parseInt(s)
            if (i < 0 || i > 255) {
                return false
            }
        }
        return true
    }

    enum class AuthLoginResult {
        INVALID_PASSWORD,
        ACCOUNT_BANNED,
        ALREADY_ON_LS,
        ALREADY_ON_GS,
        AUTH_SUCCESS
    }

    internal class PurgeThread : Thread() {
        init {
            name = "PurgeThread"
        }

        override fun run() {
            while (!isInterrupted) {
                clientsConnected.values
                        .filter { it.connectionStartTime + LOGIN_TIMEOUT < System.currentTimeMillis() }
                        .forEach { it.close(LoginFailReason.REASON_ACCESS_FAILED) }

                try {
                    Thread.sleep((LOGIN_TIMEOUT / 2).toLong())
                } catch (e: InterruptedException) {
                    return
                }

            }
        }
    }
}
