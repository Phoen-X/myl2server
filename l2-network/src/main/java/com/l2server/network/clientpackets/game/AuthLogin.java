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
package com.l2server.network.clientpackets.game;

import com.l2server.network.GameServerPacketProcessor;
import com.l2server.network.L2GameClient;
import lombok.Getter;

/**
 * This class ...
 *
 * @version $Revision: 1.9.2.3.2.4 $ $Date: 2005/03/27 15:29:30 $
 */
@Getter
public final class AuthLogin extends L2GameClientPacket {
    private static final String _C__2B_AUTHLOGIN = "[C] 2B AuthLogin";

    // loginName + keys must match what the loginserver used.
    private String loginName;
    /*
     * private final long _key1; private final long _key2; private final long _key3; private final long _key4;
     */
    private int _playKey1;
    private int _playKey2;
    private int _loginKey1;
    private int _loginKey2;

    @Override
    protected void readImpl() {
        loginName = readS().toLowerCase();
        _playKey2 = readD();
        _playKey1 = readD();
        _loginKey1 = readD();
        _loginKey2 = readD();
    }

	/*@Override
	protected void runImpl()
	{
		final L2GameClient client = getClient();
		if (loginName.isEmpty() || !client.isProtocolOk())
		{
			client.close((L2GameServerPacket) null);
			return;
		}
		SessionKey key = new SessionKey(_loginKey1, _loginKey2, _playKey1, _playKey2);
		if (Config.DEBUG)
		{
			_log.info("user:" + loginName);
			_log.info("key:" + key);
		}
		
		// avoid potential exploits
		if (client.getAccountName() == null)
		{
			// Preventing duplicate login in case client login server socket was disconnected or this packet was not sent yet
			if (LoginServerThread.getInstance().addGameServerLogin(loginName, client))
			{
				client.setAccountName(loginName);
				LoginServerThread.getInstance().addWaitingClientAndSendRequest(loginName, client, key);
			}
			else
			{
				client.close((L2GameServerPacket) null);
			}
		}
	}*/

    @Override
    public void process(GameServerPacketProcessor processor, L2GameClient client) {
        processor.process(this, client);
    }
}
