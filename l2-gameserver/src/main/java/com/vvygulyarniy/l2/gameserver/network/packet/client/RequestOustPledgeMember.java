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

/**
 * This class ...
 *
 * @version $Revision: 1.3.2.1.2.3 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestOustPledgeMember extends L2GameClientPacket {
    private static final String _C__29_REQUESTOUSTPLEDGEMEMBER = "[C] 29 RequestOustPledgeMember";

    private String _target;

    public RequestOustPledgeMember(ByteBuffer buf) {
        super(buf);
    }

    @Override
    protected void readImpl() {
        _target = readS();
    }

/*
    @Override
    protected void runImpl() {
        L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }
        if (activeChar.getClan() == null) {
            activeChar.send(SystemMessageId.YOU_ARE_NOT_A_CLAN_MEMBER);
            return;
        }
        if (!activeChar.hasClanPrivilege(ClanPrivilege.CL_DISMISS)) {
            activeChar.send(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
            return;
        }
        if (activeChar.getName().equalsIgnoreCase(_target)) {
            activeChar.send(SystemMessageId.YOU_CANNOT_DISMISS_YOURSELF);
            return;
        }

        L2Clan clan = activeChar.getClan();

        L2ClanMember member = clan.getClanMember(_target);
        if (member == null) {
            _log.warning("Target (" + _target + ") is not member of the clan");
            return;
        }
        if (member.isOnline() && member.getPlayerInstance().isInCombat()) {
            activeChar.send(SystemMessageId.CLAN_MEMBER_CANNOT_BE_DISMISSED_DURING_COMBAT);
            return;
        }

        // this also updates the database
        clan.removeClanMember(member.getObjectId(), System.currentTimeMillis() + TimeUnit.DAYS.toMillis(Config.ALT_CLAN_JOIN_DAYS));
        clan.setCharPenaltyExpiryTime(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(Config.ALT_CLAN_JOIN_DAYS));
        clan.updateClanInDB();

        SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.CLAN_MEMBER_S1_EXPELLED);
        sm.addString(member.getName());
        clan.broadcastToOnlineMembers(sm);
        activeChar.send(SystemMessageId.YOU_HAVE_SUCCEEDED_IN_EXPELLING_CLAN_MEMBER);
        activeChar.send(SystemMessageId.YOU_MUST_WAIT_BEFORE_ACCEPTING_A_NEW_MEMBER);

        // Remove the Player From the Member list
        clan.broadcastToOnlineMembers(new PledgeShowMemberListDelete(_target));

        if (member.isOnline()) {
            L2PcInstance player = member.getPlayerInstance();
            player.send(SystemMessageId.CLAN_MEMBERSHIP_TERMINATED);
        }
    }
*/


    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new RuntimeException("Not implemented yet");
    }
}
