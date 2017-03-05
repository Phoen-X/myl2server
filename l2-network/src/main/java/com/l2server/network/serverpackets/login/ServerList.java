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
package com.l2server.network.serverpackets.login;


import com.l2server.network.L2LoginClient;
import lombok.ToString;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ServerList
 * <p>
 * <pre>
 * Format: cc [cddcchhcdc]
 *
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
 *    3rd bit: won't display server name
 *    4th bit: test server (used by client?)
 * c: 0 if you don't want to display brackets in front of sever name
 * ]
 * </pre>
 * <p>
 * Server will be considered as Good when the number of online players<br>
 * is less than half the maximum. as Normal between half and 4/5<br>
 * and Full when there's more than 4/5 of the maximum number of players.
 */
@ToString
public final class ServerList extends L2LoginServerPacket {

    private final List<ServerData> _servers;
    private final int _lastServer;
    private final Map<Integer, Integer> _charsOnServers;
    private final Map<Integer, long[]> _charsToDelete;

    public ServerList(L2LoginClient client, List<ServerData> servers) {
        _servers = new ArrayList<>(servers);
        _lastServer = client.getLastServer();
        _charsOnServers = client.getCharsOnServ();
        _charsToDelete = client.getCharsWaitingDelOnServ();
    }

    @Override
    public void write(ByteBuffer buffer) {
        writeC(buffer, 0x04);
        writeC(buffer, _servers.size());
        writeC(buffer, _lastServer);
        for (ServerData server : _servers) {
            writeC(buffer, server._serverId); // server id

            writeC(buffer, server._ip[0] & 0xff);
            writeC(buffer, server._ip[1] & 0xff);
            writeC(buffer, server._ip[2] & 0xff);
            writeC(buffer, server._ip[3] & 0xff);

            writeD(buffer, server._port);
            writeC(buffer, server._ageLimit); // Age Limit 0, 15, 18
            writeC(buffer, server._pvp ? 0x01 : 0x00);
            writeH(buffer, server._currentPlayers);
            writeH(buffer, server._maxPlayers);
            writeC(buffer, server._status == ServerStatus.STATUS_DOWN ? 0x00 : 0x01);
            writeD(buffer, server._serverType); // 1: Normal, 2: Relax, 4: Public Test, 8: No Label, 16: Character Creation Restricted, 32: Event, 64: Free
            writeC(buffer, server._brackets ? 0x01 : 0x00);
        }
        writeH(buffer, 0x00); // unknown
        if (_charsOnServers != null) {
            writeC(buffer, _charsOnServers.size());
            for (int servId : _charsOnServers.keySet()) {
                writeC(buffer, servId);
                writeC(buffer, _charsOnServers.get(servId));
                if ((_charsToDelete == null) || !_charsToDelete.containsKey(servId)) {
                    writeC(buffer, 0x00);
                } else {
                    writeC(buffer, _charsToDelete.get(servId).length);
                    for (long deleteTime : _charsToDelete.get(servId)) {
                        writeD(buffer, (int) ((deleteTime - System.currentTimeMillis()) / 1000));
                    }
                }
            }
        } else {
            writeC(buffer, 0x00);
        }
    }

    @ToString
    public static class ServerData {
        protected byte[] _ip;
        protected int _port;
        protected int _ageLimit;
        protected boolean _pvp;
        protected int _currentPlayers;
        protected int _maxPlayers;
        protected boolean _brackets;
        protected boolean _clock;
        protected int _status;
        protected int _serverId;
        protected int _serverType;

        public ServerData(byte[] serverIp,
                          int id,
                          int status,
                          int port,
                          boolean pvp,
                          int serverType,
                          int maxPlayers,
                          int currentPlayers) {
            _ip = new byte[4];
            _ip[0] = serverIp[0];
            _ip[1] = serverIp[1];
            _ip[2] = serverIp[2];
            _ip[3] = serverIp[3];

            _port = port;
            _pvp = pvp;
            _serverType = serverType;
            _currentPlayers = currentPlayers;
            _maxPlayers = maxPlayers;
            _ageLimit = 0;
            _brackets = false;
            // If server GM-only - show status only to GMs
            _status = status;
            _serverId = id;
        }
    }
}
