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
package com.l2server.network.login


import com.l2server.network.SessionKey
import com.l2server.network.serverpackets.login.L2LoginServerPacket
import com.l2server.network.serverpackets.login.LoginFail
import com.l2server.network.serverpackets.login.PlayFail
import com.l2server.network.util.crypt.LoginCrypt
import com.l2server.network.util.crypt.ScrambledKeyPair
import org.slf4j.LoggerFactory.getLogger
import java.io.IOException
import java.nio.ByteBuffer
import java.security.interfaces.RSAPrivateKey
import java.util.*

/**
 * Represents a client connected into the LoginServer

 * @author KenM
 */
class L2LoginClient(private val connection: LoginClientConnection,
                    private val _scrambledPair: ScrambledKeyPair,
                    val blowfishKey: ByteArray) {
    // Crypt
    val loginCrypt: LoginCrypt
    val sessionId: Int
    val connectionStartTime: Long
    var state: LoginClientState? = null
    var account: String? = null
    var accessLevel: Int = 0
    var lastServer: Int = 0
    var sessionKey: SessionKey? = null
    private var _joinedGS: Boolean = false
    private var charsOnServers = hashMapOf<Int, Int>()
    private var charsToDelete = hashMapOf<Int, LongArray>()

    init {
        this.state = LoginClientState.CONNECTED
        this.sessionId = rnd.nextInt()
        this.connectionStartTime = System.currentTimeMillis()
        this.loginCrypt = LoginCrypt()
        this.loginCrypt.setKey(blowfishKey)
    }

    fun decrypt(buf: ByteBuffer, size: Int): Boolean {

        try {
            val isChecksumValid = loginCrypt.decrypt(buf.array(), buf.position(), size)
            if (!isChecksumValid) {

                connection.disconnect()
                return false
            }
            return true
        } catch (e: IOException) {
            connection.disconnect()
            return false
        }

    }

    fun encrypt(buf: ByteBuffer, size: Int): Boolean {
        /*final int offset = buf.position();
        try {
            size = loginCrypt.encrypt(buf.array(), offset, size);
        } catch (IOException e) {

            return false;
        }
        buf.position(offset + size);
        return true;*/
        try {
            loginCrypt.encrypt(buf, size)
            return true
        } catch (e: Exception) {
            return false
        }

    }

    val scrambledModulus: ByteArray
        get() = _scrambledPair._scrambledModulus

    val rsaPrivateKey: RSAPrivateKey
        get() = _scrambledPair._pair.private as RSAPrivateKey

    fun hasJoinedGS(): Boolean {
        return _joinedGS
    }

    fun setJoinedGS(`val`: Boolean) {
        _joinedGS = `val`
    }

    fun sendPacket(lsp: L2LoginServerPacket) {
        log.info("sending {}", lsp)
        connection.send(lsp)
    }

    fun close(reason: LoginFail.LoginFailReason) {
        close(LoginFail(reason))
    }

    fun close(reason: PlayFail.PlayFailReason) {
        close(PlayFail(reason))
    }

    fun close(lsp: L2LoginServerPacket) {
        connection.send(lsp)
        connection.disconnect()
    }

    fun setCharsOnServer(servId: Int, chars: Int) {
        charsOnServers.put(servId, chars)
    }

    val charsOnServ: HashMap<Int, Int>
        get() = charsOnServers

    fun serCharsWaitingDelOnServ(servId: Int, charsToDel: LongArray) {
        charsToDelete.put(servId, charsToDel)
    }

    val charsWaitingDelOnServ: Map<Int, LongArray>
        get() = charsToDelete

    enum class LoginClientState {
        CONNECTED,
        AUTHED_GG,
        AUTHED_LOGIN
    }

    companion object {
        private val rnd = Random()
        private val log = getLogger(L2LoginClient::class.java)
    }
}
