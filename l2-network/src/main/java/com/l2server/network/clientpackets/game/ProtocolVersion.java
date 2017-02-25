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
import lombok.ToString;

/**
 * This class ...
 *
 * @version $Revision: 1.5.2.8.2.8 $ $Date: 2005/04/02 10:43:04 $
 */
@ToString
public final class ProtocolVersion extends L2GameClientPacket {
    private static final String _C__0E_PROTOCOLVERSION = "[C] 0E ProtocolVersion";

    @Getter
    private int version;

    @Override
    protected void readImpl() {
        version = readD();
    }

	/*@Override
    protected void runImpl()
	{
		// this packet is never encrypted
		if (version == -2)
		{
			// this is just a ping attempt from the new C2 client
			getClient().close((L2GameServerPacket) null);
		}
		else if (!Config.PROTOCOL_LIST.contains(version))
		{
			LogRecord record = new LogRecord(Level.WARNING, "Wrong protocol");
			record.setParameters(new Object[]
			{
				version,
				getClient()
			});
			_logAccounting.log(record);
			KeyPacket pk = new KeyPacket(getClient().enableCrypt(), 0);
			getClient().setProtocolOk(false);
			getClient().close(pk);
		}
		else
		{
			if (Config.DEBUG)
			{
				_log.fine("Client Protocol Revision is ok: " + version);
			}
			
			KeyPacket pk = new KeyPacket(getClient().enableCrypt(), 1);
			getClient().sendPacket(pk);
			getClient().setProtocolOk(true);
		}
	}*/

    @Override
    public void process(GameServerPacketProcessor processor, L2GameClient client) {
        processor.process(this, client);
    }
}
