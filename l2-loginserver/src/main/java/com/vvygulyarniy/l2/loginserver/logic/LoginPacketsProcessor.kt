package com.vvygulyarniy.l2.loginserver.logic

import com.vvygulyarniy.l2.loginserver.GameServerTable
import com.vvygulyarniy.l2.loginserver.LoginController
import com.vvygulyarniy.l2.loginserver.netty.login.ClientPacketProcessor
import com.vvygulyarniy.l2.loginserver.netty.login.L2LoginClient
import com.vvygulyarniy.l2.loginserver.netty.login.L2LoginClient.LoginClientState.AUTHED_LOGIN
import com.vvygulyarniy.l2.loginserver.netty.login.ServerStatus
import com.vvygulyarniy.l2.loginserver.netty.packet.client.AuthGameGuard
import com.vvygulyarniy.l2.loginserver.netty.packet.client.RequestAuthLogin
import com.vvygulyarniy.l2.loginserver.netty.packet.client.RequestServerList
import com.vvygulyarniy.l2.loginserver.netty.packet.client.RequestServerLogin
import com.vvygulyarniy.l2.loginserver.netty.packet.server.AccountKicked
import com.vvygulyarniy.l2.loginserver.netty.packet.server.AccountKicked.AccountKickedReason.REASON_PERMANENTLY_BANNED
import com.vvygulyarniy.l2.loginserver.netty.packet.server.GGAuth
import com.vvygulyarniy.l2.loginserver.netty.packet.server.LoginFail.LoginFailReason.*
import com.vvygulyarniy.l2.loginserver.netty.packet.server.PlayFail.PlayFailReason.REASON_SERVER_OVERLOADED
import com.vvygulyarniy.l2.loginserver.netty.packet.server.PlayOk
import com.vvygulyarniy.l2.loginserver.netty.packet.server.ServerList
import com.vvygulyarniy.l2.loginserver.netty.packet.server.ServerList.ServerData
import java.net.InetAddress
import java.net.UnknownHostException
import java.security.GeneralSecurityException
import java.util.*
import javax.crypto.Cipher

class LoginPacketsProcessor(private val gameServerTable: GameServerTable,
                            private val loginController: LoginController) : ClientPacketProcessor {

    override fun process(packet: RequestServerList, client: L2LoginClient) {
        if (client.sessionKey!!.checkLoginPair(packet.sessionKey1, packet.sessionKey2)) {
            try {
                val servers = serverList
                client.sendPacket(ServerList(client, servers))
            } catch (e: UnknownHostException) {
                log.error("Cannot resolve address of gameserver host", e)
                client.close(REASON_ACCESS_FAILED)
            }

        } else {
            client.close(REASON_ACCESS_FAILED)
        }
    }

    override fun process(packet: RequestAuthLogin, client: L2LoginClient) {
        val decrypted: ByteArray
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
            val result = lc.tryCheckinAccount(client, info)
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
        }

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

    override fun process(packet: RequestServerLogin, client: L2LoginClient) {
        val sk = client.sessionKey

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
    }

    override fun process(packet: AuthGameGuard, client: L2LoginClient) {
        if (packet.sessionId == client.sessionId.toInt()) {
            client.state = L2LoginClient.LoginClientState.AUTHED_GG
            client.sendPacket(GGAuth(client.sessionId.toInt()))
        } else {
            client.close(REASON_ACCESS_FAILED)
        }
    }

    companion object {
        private val log = org.slf4j.LoggerFactory.getLogger(LoginPacketsProcessor::class.java)
    }
}
