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

/**
 * Format: (ch) dSdS
 *
 * @author -Wooden-
 */
public final class RequestPledgeReorganizeMember extends L2GameClientPacket {
    private static final String _C__D0_2C_REQUESTPLEDGEREORGANIZEMEMBER = "[C] D0:2C RequestPledgeReorganizeMember";

    private int _isMemberSelected;
    private String _memberName;
    private int _newPledgeType;
    private String _selectedMember;

    public RequestPledgeReorganizeMember(ByteBuffer buf) {
        super(buf);
    }

    @Override
    protected void readImpl() {
        _isMemberSelected = readD();
        _memberName = readS();
        _newPledgeType = readD();
        _selectedMember = readS();
    }

/*
    @Override
    protected void runImpl() {
        if (_isMemberSelected == 0) {
            return;
        }

        final L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }

        final L2Clan clan = activeChar.getClan();
        if (clan == null) {
            return;
        }

        if (!activeChar.hasClanPrivilege(ClanPrivilege.CL_MANAGE_RANKS)) {
            return;
        }

        final L2ClanMember member1 = clan.getClanMember(_memberName);
        if ((member1 == null) || (member1.getObjectId() == clan.getLeaderId())) {
            return;
        }

        final L2ClanMember member2 = clan.getClanMember(_selectedMember);
        if ((member2 == null) || (member2.getObjectId() == clan.getLeaderId())) {
            return;
        }

        final int oldPledgeType = member1.getPledgeType();
        if (oldPledgeType == _newPledgeType) {
            return;
        }

        member1.setPledgeType(_newPledgeType);
        member2.setPledgeType(oldPledgeType);
        clan.broadcastClanStatus();
    }
*/


    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}
