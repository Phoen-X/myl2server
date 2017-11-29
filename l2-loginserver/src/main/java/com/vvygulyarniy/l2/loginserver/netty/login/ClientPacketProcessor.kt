package com.vvygulyarniy.l2.loginserver.netty.login

import com.vvygulyarniy.l2.loginserver.netty.packet.client.AuthGameGuard
import com.vvygulyarniy.l2.loginserver.netty.packet.client.RequestAuthLogin
import com.vvygulyarniy.l2.loginserver.netty.packet.client.RequestServerList
import com.vvygulyarniy.l2.loginserver.netty.packet.client.RequestServerLogin

/**
 * Created by Phoen-X on 16.02.2017.
 */
interface ClientPacketProcessor {
    fun process(packet: RequestServerList, client: L2LoginClient)

    fun process(packet: RequestAuthLogin, client: L2LoginClient)

    fun process(requestServerLogin: RequestServerLogin, client: L2LoginClient)

    fun process(authGameGuard: AuthGameGuard, client: L2LoginClient)
}
