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
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public final class AllyLeave extends L2GameClientPacket {
    private static final String _C__8E_ALLYLEAVE = "[C] 8E AllyLeave";

    @Override
    protected void readImpl() {
    }

    /*@Override
    protected void runImpl() {
        L2PcInstance player = getClient().getActiveChar();
        if (player == null) {
            return;
        }
        if (player.getClan() == null) {
            player.send(SystemMessageId.YOU_ARE_NOT_A_CLAN_MEMBER);
            return;
        }
        if (!player.isClanLeader()) {
            player.send(SystemMessageId.ONLY_CLAN_LEADER_WITHDRAW_ALLY);
            return;
        }
        L2Clan clan = player.getClan();
        if (clan.getAllyId() == 0) {
            player.send(SystemMessageId.NO_CURRENT_ALLIANCES);
            return;
        }
        if (clan.getId() == clan.getAllyId()) {
            player.send(SystemMessageId.ALLIANCE_LEADER_CANT_WITHDRAW);
            return;
        }

        long currentTime = System.currentTimeMillis();
        clan.setAllyId(0);
        clan.setAllyName(null);
        clan.changeAllyCrest(0, true);
        clan.setAllyPenaltyExpiryTime(currentTime + (Config.ALT_ALLY_JOIN_DAYS_WHEN_LEAVED * 86400000L), L2Clan.PENALTY_TYPE_CLAN_LEAVED); // 24*60*60*1000 = 86400000
        clan.updateClanInDB();

        player.send(SystemMessageId.YOU_HAVE_WITHDRAWN_FROM_ALLIANCE);
    }*/

    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}