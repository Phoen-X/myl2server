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
package com.vvygulyarniy.l2.loginserver.netty.packet.server


import com.vvygulyarniy.l2.loginserver.netty.login.L2LoginClient
import com.vvygulyarniy.l2.loginserver.netty.login.ServerStatus
import java.nio.ByteBuffer

/**
 * ServerList
 *
 *
 * <pre>
 * Format: cc [cddcchhcdc]

 * c: server list size (number of servers)
 * c: ?
 * [ (repeat for each servers)
 * c: server id (ignored by client?)
 * d: server ip
 * d: server port
 * c: age limit (used by client?)
 * c: pvp or not (used by client?)
 * h: current number of players
 * h: max number of players
 * c: 0 if server is down
 * d: 2nd bit: clock
 * 3rd bit: won't display server name
 * 4th bit: test server (used by client?)
 * c: 0 if you don't want to display brackets in front of sever name
 * ]
</pre> *
 *
 *
 * Server will be considered as Good when the number of online players<br></br>
 * is less than half the maximum. as Normal between half and 4/5<br></br>
 * and Full when there's more than 4/5 of the maximum number of players.
 */
data class ServerList(val client: L2LoginClient, val servers: List<ServerData>) : L2LoginServerPacket() {

    private val lastServer: Int = client.lastServer
    private val charsOnServers: Map<Int, Int>
    private val charsToDelete: Map<Int, LongArray>?

    init {
        charsOnServers = client.charsOnServ
        charsToDelete = client.charsWaitingDelOnServ
    }

    override fun write(buffer: ByteBuffer) {
        writeC(buffer, 0x04)
        writeC(buffer, servers.size)
        writeC(buffer, lastServer)
        for (server in servers) {
            writeC(buffer, server.serverId) // server id

            writeC(buffer, server.ip[0].toInt() and 0xff)
            writeC(buffer, server.ip[1].toInt() and 0xff)
            writeC(buffer, server.ip[2].toInt() and 0xff)
            writeC(buffer, server.ip[3].toInt() and 0xff)

            writeD(buffer, server.port)
            writeC(buffer, server.ageLimit) // Age Limit 0, 15, 18
            writeC(buffer, if (server.pvp) 0x01 else 0x00)
            writeH(buffer, server.currentPlayers)
            writeH(buffer, server.maxPlayers)
            writeC(buffer, if (server.status == ServerStatus.DOWN.code) 0x00 else 0x01)
            writeD(buffer, server.serverType) // 1: Normal, 2: Relax, 4: Public Test, 8: No Label, 16: Character Creation Restricted, 32: Event, 64: Free
            writeC(buffer, if (server.brackets) 0x01 else 0x00)
        }
        writeH(buffer, 0x00) // unknown
        if (charsOnServers.isNotEmpty()) {
            writeC(buffer, charsOnServers.size)
            for ((servId, count) in charsOnServers) {
                writeC(buffer, servId)
                writeC(buffer, count)
                if (charsToDelete == null || !charsToDelete.containsKey(servId)) {
                    writeC(buffer, 0x00)
                } else {
                    val charsToRemove = charsToDelete[servId] ?: longArrayOf()
                    writeC(buffer, charsToRemove.size)
                    for (deleteTime in charsToRemove) {
                        writeD(buffer, ((deleteTime - System.currentTimeMillis()) / 1000).toInt())
                    }
                }
            }
        } else {
            writeC(buffer, 0x00)
        }
    }

    class ServerData(serverIp: ByteArray,
                     var serverId: Int,
                     var status: Int,
                     var port: Int,
                     var pvp: Boolean,
                     var serverType: Int,
                     var maxPlayers: Int,
                     var currentPlayers: Int) {
        var ip: ByteArray = ByteArray(4)
        var ageLimit: Int = 0
        var brackets: Boolean = false
        var clock: Boolean = false

        init {
            ip[0] = serverIp[0]
            ip[1] = serverIp[1]
            ip[2] = serverIp[2]
            ip[3] = serverIp[3]
            ageLimit = 0
            brackets = false
        }// If server GM-only - show status only to GMs

        override fun toString(): String {
            return "ServerData(ip=" + java.util.Arrays.toString(this.ip) + ", port=" + this.port + ", ageLimit=" + this.ageLimit + ", pvp=" + this.pvp + ", currentPlayers=" + this.currentPlayers + ", maxPlayers=" + this.maxPlayers + ", brackets=" + this.brackets + ", clock=" + this.clock + ", status=" + this.status + ", serverId=" + this.serverId + ", serverType=" + this.serverType + ")"
        }
    }
}
