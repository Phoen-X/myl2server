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

/**
 * This class ...
 *
 * @version $Revision: 1.4.2.1.2.3 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestAnswerJoinPledge extends L2GameClientPacket {
    private static final String _C__27_REQUESTANSWERJOINPLEDGE = "[C] 27 RequestAnswerJoinPledge";

    private int _answer;

    @Override
    protected void readImpl() {
        _answer = readD();
    }

    /*@Override
    protected void runImpl() {
        L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }

        L2PcInstance requestor = activeChar.getRequest().getPartner();
        if (requestor == null) {
            return;
        }

        if (_answer == 0) {
            SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.YOU_DID_NOT_RESPOND_TO_S1_CLAN_INVITATION);
            sm.addString(requestor.getName());
            activeChar.send(sm);
            sm = SystemMessage.getSystemMessage(SystemMessageId.S1_DID_NOT_RESPOND_TO_CLAN_INVITATION);
            sm.addString(activeChar.getName());
            requestor.send(sm);
        } else {
            if (!(requestor.getRequest().getRequestPacket() instanceof RequestJoinPledge)) {
                return; // hax
            }

            RequestJoinPledge requestPacket = (RequestJoinPledge) requestor.getRequest().getRequestPacket();
            L2Clan clan = requestor.getClan();
            // we must double check this cause during response time conditions can be changed, i.e. another player could join clan
            if (clan.checkClanJoinCondition(requestor, activeChar, requestPacket.getPledgeType())) {
                activeChar.send(new JoinPledge(requestor.getClanId()));

                activeChar.setPledgeType(requestPacket.getPledgeType());
                if (requestPacket.getPledgeType() == L2Clan.SUBUNIT_ACADEMY) {
                    activeChar.setPowerGrade(9); // Academy
                    activeChar.setLvlJoinedAcademy(activeChar.getLevel());
                } else {
                    activeChar.setPowerGrade(5); // new member starts at 5, not confirmed
                }

                clan.addClanMember(activeChar);
                activeChar.setClanPrivileges(activeChar.getClan().getRankPrivs(activeChar.getPowerGrade()));
                activeChar.send(SystemMessageId.ENTERED_THE_CLAN);

                SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_JOINED_CLAN);
                sm.addString(activeChar.getName());
                clan.broadcastToOnlineMembers(sm);

                if (activeChar.getClan().getCastleId() > 0) {
                    CastleManager.getInstance().getCastleByOwner(activeChar.getClan()).giveResidentialSkills(activeChar);
                }
                if (activeChar.getClan().getFortId() > 0) {
                    FortManager.getInstance().getFortByOwner(activeChar.getClan()).giveResidentialSkills(activeChar);
                }
                activeChar.sendSkillList();

                clan.broadcastToOtherOnlineMembers(new PledgeShowMemberListAdd(activeChar), activeChar);
                clan.broadcastToOnlineMembers(new PledgeShowInfoUpdate(clan));

                // this activates the clan tab on the new member
                activeChar.send(new PledgeShowMemberListAll(clan, activeChar));
                activeChar.setClanJoinExpiryTime(0);
                activeChar.broadcastUserInfo();
            }
        }

        activeChar.getRequest().onRequestResponse();
    }
    */
    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}
