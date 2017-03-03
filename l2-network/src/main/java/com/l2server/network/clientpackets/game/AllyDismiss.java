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
import lombok.ToString;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@ToString
public final class AllyDismiss extends L2GameClientPacket {
    private static final String _C__8F_ALLYDISMISS = "[C] 8F AllyDismiss";

    private String _clanName;

    @Override
    protected void readImpl() {
        _clanName = readS();
    }

    @Override
    public void process(GameServerPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }

   /* @Override
    protected void runImpl() {
        if (_clanName == null) {
            return;
        }
        L2PcInstance player = getClient().getActiveChar();
        if (player == null) {
            return;
        }
        if (player.getClan() == null) {
            player.send(SystemMessageId.YOU_ARE_NOT_A_CLAN_MEMBER);
            return;
        }
        L2Clan leaderClan = player.getClan();
        if (leaderClan.getAllyId() == 0) {
            player.send(SystemMessageId.NO_CURRENT_ALLIANCES);
            return;
        }
        if (!player.isClanLeader() || (leaderClan.getId() != leaderClan.getAllyId())) {
            player.send(SystemMessageId.FEATURE_ONLY_FOR_ALLIANCE_LEADER);
            return;
        }
        L2Clan clan = ClanTable.getInstance().getClanByName(_clanName);
        if (clan == null) {
            player.send(SystemMessageId.CLAN_DOESNT_EXISTS);
            return;
        }
        if (clan.getId() == leaderClan.getId()) {
            player.send(SystemMessageId.ALLIANCE_LEADER_CANT_WITHDRAW);
            return;
        }
        if (clan.getAllyId() != leaderClan.getAllyId()) {
            player.send(SystemMessageId.DIFFERENT_ALLIANCE);
            return;
        }

        long currentTime = System.currentTimeMillis();
        leaderClan.setAllyPenaltyExpiryTime(currentTime + (Config.ALT_ACCEPT_CLAN_DAYS_WHEN_DISMISSED * 86400000L), L2Clan.PENALTY_TYPE_DISMISS_CLAN); // 24*60*60*1000 = 86400000
        leaderClan.updateClanInDB();

        clan.setAllyId(0);
        clan.setAllyName(null);
        clan.changeAllyCrest(0, true);
        clan.setAllyPenaltyExpiryTime(currentTime + (Config.ALT_ALLY_JOIN_DAYS_WHEN_DISMISSED * 86400000L), L2Clan.PENALTY_TYPE_CLAN_DISMISSED); // 24*60*60*1000 = 86400000
        clan.updateClanInDB();

        player.send(SystemMessageId.YOU_HAVE_EXPELED_A_CLAN);
    }
*/
}
