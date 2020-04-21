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

public final class RequestSurrenderPledgeWar extends L2GameClientPacket {
    private static final String _C__07_REQUESTSURRENDERPLEDGEWAR = "[C] 07 RequestSurrenderPledgeWar";

    private String _pledgeName;

    public RequestSurrenderPledgeWar(ByteBuffer buf) {
        super(buf);
    }

    @Override
    protected void readImpl() {
        _pledgeName = readS();
    }

    /*@Override
    protected void runImpl() {
        L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }
        L2Clan _clan = activeChar.getClan();
        if (_clan == null) {
            return;
        }
        L2Clan clan = ClanTable.getInstance().getClanByName(_pledgeName);

        if (clan == null) {
            activeChar.sendMessage("No such clan.");
            activeChar.send(ActionFailed.STATIC_PACKET);
            return;
        }

        _log.info("RequestSurrenderPledgeWar by " + getClient().getActiveChar().getClan().getName() + " with " + _pledgeName);

        if (!_clan.isAtWarWith(clan.getId())) {
            activeChar.sendMessage("You aren't at war with this clan.");
            activeChar.send(ActionFailed.STATIC_PACKET);
            return;
        }

        SystemMessage msg = SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_SURRENDERED_TO_THE_S1_CLAN);
        msg.addString(_pledgeName);
        activeChar.send(msg);
        ClanTable.getInstance().deleteclanswars(_clan.getId(), clan.getId());
    }*/


    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new RuntimeException("Not implemented yet");
    }
}