package com.l2server.network

import com.l2server.network.clientpackets.login.AuthGameGuard
import com.l2server.network.clientpackets.login.RequestAuthLogin
import com.l2server.network.clientpackets.login.RequestServerList
import com.l2server.network.clientpackets.login.RequestServerLogin
import com.l2server.network.login.L2LoginClient

/**
 * Created by Phoen-X on 16.02.2017.
 */
interface ClientPacketProcessor {
    fun process(packet: RequestServerList, client: L2LoginClient)

    fun process(packet: RequestAuthLogin, client: L2LoginClient)

    fun process(requestServerLogin: RequestServerLogin, client: L2LoginClient)

    fun process(authGameGuard: AuthGameGuard, client: L2LoginClient)
}
