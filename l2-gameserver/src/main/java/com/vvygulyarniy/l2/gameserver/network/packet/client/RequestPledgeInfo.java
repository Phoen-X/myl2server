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
package com.vvygulyarniy.l2.gameserver.network.packet.client;

import com.vvygulyarniy.l2.gameserver.network.L2GameClient;
import com.vvygulyarniy.l2.gameserver.network.packet.L2ClientPacketProcessor;

import java.nio.ByteBuffer;

public final class RequestPledgeInfo extends L2GameClientPacket {
    private static final String _C__65_REQUESTPLEDGEINFO = "[C] 65 RequestPledgeInfo";

    private int _clanId;

    public RequestPledgeInfo(ByteBuffer buf) {
        super(buf);
    }

    @Override
    protected void readImpl() {
        _clanId = readD();
    }

/*
    @Override
    protected void runImpl() {
        if (Config.DEBUG) {
            _log.log(Level.FINE, "Info for clan " + _clanId + " requested");
        }

        final L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }

        final L2Clan clan = ClanTable.getInstance().getClan(_clanId);
        if (clan == null) {
            if (Config.DEBUG) {
                _log.warning(getType() + ": Clan data for clanId " + _clanId + " is missing for player " + activeChar);
            }
            return; // we have no clan data ?!? should not happen
        }

        activeChar.sendPacket(new PledgeInfo(clan));
    }

*/

    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new RuntimeException("Not implemented yet");
    }
}
