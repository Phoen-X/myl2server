package com.vvygulyarniy.l2.loginserver.server

import com.vvygulyarniy.l2.loginserver.communication.packet.server.ServerList.ServerData
import org.springframework.stereotype.Component

@Component
class GameServersManager {
    private val servers = listOf(ServerData(byteArrayOf(127, 0, 0, 1),
                                            1,
                                            ServerStatus.GOOD.code,
                                            9999,
                                            false,
                                            1,
                                            100,
                                            90))


    fun getAvailableServers(): List<ServerData> {
        return servers
    }
}