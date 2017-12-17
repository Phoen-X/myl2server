package com.vvygulyarniy.l2.loginserver.logic

import com.l2server.network.SessionKey
import com.vvygulyarniy.l2.loginserver.communication.CommunicationManager
import com.vvygulyarniy.l2.loginserver.communication.packet.client.*
import com.vvygulyarniy.l2.loginserver.communication.packet.server.GGAuth
import com.vvygulyarniy.l2.loginserver.communication.packet.server.PlayOk
import com.vvygulyarniy.l2.loginserver.communication.packet.server.ServerList
import com.vvygulyarniy.l2.loginserver.communication.packet.server.ServerList.ServerData
import com.vvygulyarniy.l2.loginserver.model.data.SessionId
import com.vvygulyarniy.l2.loginserver.netty.login.ClientPacketProcessor
import com.vvygulyarniy.l2.loginserver.netty.login.ServerStatus
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.*

class LoginPacketsProcessor<T : Any>(private val communicationManager: CommunicationManager<T>) : ClientPacketProcessor {

    override fun process(sessionId: SessionId, packet: L2LoginClientPacket) {
        when (packet) {
            is RequestServerList -> handleServerListRequest(sessionId, packet)
            is AuthGameGuard -> handleAuthGameGuard(sessionId, packet)
            is RequestAuthLogin -> handleRequestLogin(sessionId, packet)
            is RequestServerLogin -> handleServerLoginRequest(sessionId, packet)
        }
    }

    private fun handleServerListRequest(sessionId: SessionId, packet: L2LoginClientPacket) {
        /*if (client.sessionKey!!.checkLoginPair(packet.sessionKey1, packet.sessionKey2)) {*/
            try {
                val servers = serverList
                communicationManager.sendPacket(sessionId, ServerList(servers))
            } catch (e: UnknownHostException) {
                log.error("Cannot resolve address of gameserver host", e)
                communicationManager.closeChannel(sessionId)
            }
        /*} else {
            client.close(REASON_ACCESS_FAILED)
        }*/
    }

    private fun handleRequestLogin(sessionId: SessionId, packet: RequestAuthLogin) {
        communicationManager.sendPacket(sessionId, ServerList(serverList))
        /*val decrypted: ByteArray
        try {
            val rsaCipher = Cipher.getInstance("RSA/ECB/nopadding")
            rsaCipher.init(Cipher.DECRYPT_MODE, client.rsaPrivateKey)
            decrypted = rsaCipher.doFinal(packet.raw, 0x00, 0x80)
        } catch (e: GeneralSecurityException) {
            return
        }

        try {
            packet.user = String(decrypted, 0x5E, 14).trim { it <= ' ' }.toLowerCase()
            packet.password = String(decrypted, 0x6C, 16).trim { it <= ' ' }
            val lc = loginController
            val info = lc.retriveAccountInfo(packet.user!!, packet.password!!)
            if (info == null) {
                // user or pass wrong
                client.close(REASON_USER_OR_PASS_WRONG)
                return
            }
            val result = lc.tryCheckinAccount(info)
            when (result) {
                LoginController.AuthLoginResult.AUTH_SUCCESS -> {
                    client.account = info.login
                    client.state = AUTHED_LOGIN
                    client.sessionKey = lc.assignSessionKeyToClient(info.login, client)
                    try {
                        val servers = serverList
                        client.sendPacket(ServerList(client, servers))
                    } catch (e: Exception) {
                        log.error("Cannot resolve gameserver IP", e)
                    }

                }
                LoginController.AuthLoginResult.INVALID_PASSWORD -> client.close(REASON_USER_OR_PASS_WRONG)
                LoginController.AuthLoginResult.ACCOUNT_BANNED -> {
                    client.close(AccountKicked(REASON_PERMANENTLY_BANNED))
                    return
                }
                LoginController.AuthLoginResult.ALREADY_ON_LS -> {
                    val oldClient = lc.getAuthedClient(info.login)
                    if (oldClient != null) {
                        // kick the other client
                        oldClient.close(REASON_ACCOUNT_IN_USE)
                        lc.removeAuthedLoginClient(info.login)
                    }
                    // kick also current client
                    client.close(REASON_ACCOUNT_IN_USE)
                }
            }
        } catch (e: Exception) {
            return
        }*/

    }

    private val serverList: List<ServerData>
        @Throws(UnknownHostException::class)
        get() {
            val gameServerAddr = InetAddress.getByName("localhost")
            val serverIp = Arrays.copyOf(gameServerAddr.address, 4)
            log.info("GameServer IP resolved: {}", Arrays.toString(serverIp))
            return listOf(ServerData(serverIp,
                                     1,
                                     ServerStatus.GOOD.code,
                                     9999,
                                     false,
                                     1,
                                     100,
                                     0))
        }

    private fun handleServerLoginRequest(sessionId: SessionId, packet: RequestServerLogin) {
        communicationManager.sendPacket(sessionId, PlayOk(SessionKey(1, 2, 3, 4)))
    }
    /*val sk = client.sessionKey

    // if we didnt showed the license we cant check these values
    if (sk != null && sk.checkLoginPair(packet.sessionKey1, packet.sessionKey2)) {
        if (loginController.isLoginPossible()) {
            client.setJoinedGS(true)
            client.sendPacket(PlayOk(sk))
        } else {
            client.close(REASON_SERVER_OVERLOADED)
        }
    } else {
        client.close(REASON_ACCESS_FAILED)
    }
}*/

    private fun handleAuthGameGuard(sessionId: SessionId, packet: AuthGameGuard) {
        communicationManager.sendPacket(sessionId, GGAuth(sessionId.toInt()))
        /*if (packet.sessionId == client.sessionId.toInt()) {
            client.state = L2LoginClient.LoginClientState.AUTHED_GG
            client.sendPacket(GGAuth(client.sessionId.toInt()))
        } else {
            client.close(REASON_ACCESS_FAILED)
        }*/
    }

    companion object {
        private val log = org.slf4j.LoggerFactory.getLogger(LoginPacketsProcessor::class.java)
    }
}
