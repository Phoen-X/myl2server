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
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public final class RequestAnswerJoinAlly extends L2GameClientPacket {
    private static final String _C__8D_REQUESTANSWERJOINALLY = "[C] 8D RequestAnswerJoinAlly";

    private int _response;

    @Override
    protected void readImpl() {
        _response = readD();
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

        if (_response == 0) {
            activeChar.send(SystemMessageId.YOU_DID_NOT_RESPOND_TO_ALLY_INVITATION);
            requestor.send(SystemMessageId.NO_RESPONSE_TO_ALLY_INVITATION);
        } else {
            if (!(requestor.getRequest().getRequestPacket() instanceof RequestJoinAlly)) {
                return; // hax
            }

            L2Clan clan = requestor.getClan();
            // we must double check this cause of hack
            if (clan.checkAllyJoinCondition(requestor, activeChar)) {
                // TODO: Need correct message id
                requestor.send(SystemMessageId.YOU_HAVE_SUCCEEDED_INVITING_FRIEND);
                activeChar.send(SystemMessageId.YOU_ACCEPTED_ALLIANCE);

                activeChar.getClan().setAllyId(clan.getAllyId());
                activeChar.getClan().setAllyName(clan.getAllyName());
                activeChar.getClan().setAllyPenaltyExpiryTime(0, 0);
                activeChar.getClan().changeAllyCrest(clan.getAllyCrestId(), true);
                activeChar.getClan().updateClanInDB();
            }
        }

        activeChar.getRequest().onRequestResponse();
    }
    */
    @Override
    public void process(GameServerPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}