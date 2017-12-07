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

import java.nio.ByteBuffer;

public final class RequestStartPledgeWar extends L2GameClientPacket {
    private static final String _C__03_REQUESTSTARTPLEDGEWAR = "[C] 03 RequestStartPledgewar";

    private String _pledgeName;

    public RequestStartPledgeWar(ByteBuffer buf) {
        super(buf);
    }

    @Override
    protected void readImpl() {
        _pledgeName = readS();
    }

    /*@Override
    protected void runImpl() {
        L2PcInstance player = getClient().getActiveChar();
        if (player == null) {
            return;
        }

        L2Clan _clan = getClient().getActiveChar().getClan();
        if (_clan == null) {
            return;
        }

        if ((_clan.getLevel() < 3) || (_clan.getMembersCount() < Config.ALT_CLAN_MEMBERS_FOR_WAR)) {
            player.send(SystemMessage.getSystemMessage(SystemMessageId.CLAN_WAR_DECLARED_IF_CLAN_LVL3_OR_15_MEMBER));
            player.send(ActionFailed.STATIC_PACKET);
            return;
        } else if (!player.hasClanPrivilege(ClanPrivilege.CL_PLEDGE_WAR)) {
            player.send(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
            player.send(ActionFailed.STATIC_PACKET);
            return;
        }

        L2Clan clan = ClanTable.getInstance().getClanByName(_pledgeName);
        if (clan == null) {
            player.send(SystemMessage.getSystemMessage(SystemMessageId.CLAN_WAR_CANNOT_DECLARED_CLAN_NOT_EXIST));
            player.send(ActionFailed.STATIC_PACKET);
            return;
        } else if ((_clan.getAllyId() == clan.getAllyId()) && (_clan.getAllyId() != 0)) {
            player.send(SystemMessage.getSystemMessage(SystemMessageId.CLAN_WAR_AGAINST_A_ALLIED_CLAN_NOT_WORK));
            player.send(ActionFailed.STATIC_PACKET);
            return;
        } else if ((clan.getLevel() < 3) || (clan.getMembersCount() < Config.ALT_CLAN_MEMBERS_FOR_WAR)) {
            player.send(SystemMessage.getSystemMessage(SystemMessageId.CLAN_WAR_DECLARED_IF_CLAN_LVL3_OR_15_MEMBER));
            player.send(ActionFailed.STATIC_PACKET);
            return;
        } else if (_clan.isAtWarWith(clan.getId())) {
            SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.ALREADY_AT_WAR_WITH_S1_WAIT_5_DAYS);
            sm.addString(clan.getName());
            player.send(sm);
            player.send(ActionFailed.STATIC_PACKET);
            return;
        }

        ClanTable.getInstance().storeclanswars(player.getClanId(), clan.getId());

        for (L2PcInstance member : _clan.getOnlineMembers(0)) {
            member.broadcastUserInfo();
        }

        for (L2PcInstance member : clan.getOnlineMembers(0)) {
            member.broadcastUserInfo();
        }
    }
    */
    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}
