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
 * @version $Revision: 1.3.4.2 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestJoinAlly extends L2GameClientPacket {
    private static final String _C__8C_REQUESTJOINALLY = "[C] 8C RequestJoinAlly";

    private int _id;

    public RequestJoinAlly(ByteBuffer buf) {
        super(buf);
    }

    @Override
    protected void readImpl() {
        _id = readD();
    }

    /*@Override
    protected void runImpl() {
        L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }

        L2PcInstance ob = L2World.getInstance().getPlayer(_id);

        if (ob == null) {
            activeChar.send(SystemMessageId.YOU_HAVE_INVITED_THE_WRONG_TARGET);
            return;
        }

        if (activeChar.getClan() == null) {
            activeChar.send(SystemMessageId.YOU_ARE_NOT_A_CLAN_MEMBER);
            return;
        }

        L2PcInstance target = ob;
        L2Clan clan = activeChar.getClan();
        if (!clan.checkAllyJoinCondition(activeChar, target)) {
            return;
        }
        if (!activeChar.getRequest().setRequest(target, this)) {
            return;
        }

        SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S2_ALLIANCE_LEADER_OF_S1_REQUESTED_ALLIANCE);
        sm.addString(activeChar.getClan().getAllyName());
        sm.addString(activeChar.getName());
        target.send(sm);
        target.send(new AskJoinAlly(activeChar.getObjectId(), activeChar.getClan().getAllyName()));
    }
    */
    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new RuntimeException("Not implemented yet");
    }
}
