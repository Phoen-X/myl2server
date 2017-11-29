package com.vvygulyarniy.l2.loginserver.netty.login

import com.vvygulyarniy.l2.loginserver.netty.packet.server.L2LoginServerPacket

/**
 * Phoen-X on 04.07.2017.
 */
interface LoginClientConnection {
    fun send(packet: L2LoginServerPacket)

    fun disconnect()
}
