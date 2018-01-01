package com.l2server.network.communication

import com.l2server.network.ServerPacket


interface ClientCommunicator {
    fun send(packet: ServerPacket)

    fun disconnect()
}