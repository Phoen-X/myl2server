package com.vvygulyarniy.l2.loginserver.communication

import com.vvygulyarniy.l2.loginserver.communication.packet.server.L2LoginServerPacket


interface ClientCommunicator {
    fun send(packet: L2LoginServerPacket)

    fun disconnect()
}