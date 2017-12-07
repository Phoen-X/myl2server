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
 * Format:(ch) ddd
 *
 * @author -Wooden-
 */
public final class RequestDuelAnswerStart extends L2GameClientPacket {
    private static final String _C__D0_1C_REQUESTDUELANSWERSTART = "[C] D0:1C RequestDuelAnswerStart";
    private int _partyDuel;
    @SuppressWarnings("unused")
    private int _unk1;
    private int _response;

    public RequestDuelAnswerStart(ByteBuffer buf) {
        super(buf);
    }

    @Override
    protected void readImpl() {
        _partyDuel = readD();
        _unk1 = readD();
        _response = readD();
    }

    /*@Override
    protected void runImpl() {
        L2PcInstance player = getClient().getActiveChar();
        if (player == null) {
            return;
        }

        L2PcInstance requestor = player.getActiveRequester();
        if (requestor == null) {
            return;
        }

        if (_response == 1) {
            SystemMessage msg1 = null, msg2 = null;
            if (requestor.isInDuel()) {
                msg1 = SystemMessage.getSystemMessage(SystemMessageId.C1_CANNOT_DUEL_BECAUSE_C1_IS_ALREADY_ENGAGED_IN_A_DUEL);
                msg1.addString(requestor.getName());
                player.send(msg1);
                return;
            } else if (player.isInDuel()) {
                player.send(SystemMessageId.YOU_ARE_UNABLE_TO_REQUEST_A_DUEL_AT_THIS_TIME);
                return;
            }

            if (_partyDuel == 1) {
                msg1 = SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_ACCEPTED_C1_CHALLENGE_TO_A_PARTY_DUEL_THE_DUEL_WILL_BEGIN_IN_A_FEW_MOMENTS);
                msg1.addString(requestor.getName());

                msg2 = SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_ACCEPTED_YOUR_CHALLENGE_TO_DUEL_AGAINST_THEIR_PARTY_THE_DUEL_WILL_BEGIN_IN_A_FEW_MOMENTS);
                msg2.addString(player.getName());
            } else {
                msg1 = SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_ACCEPTED_C1_CHALLENGE_TO_A_DUEL_THE_DUEL_WILL_BEGIN_IN_A_FEW_MOMENTS);
                msg1.addString(requestor.getName());

                msg2 = SystemMessage.getSystemMessage(SystemMessageId.C1_HAS_ACCEPTED_YOUR_CHALLENGE_TO_A_DUEL_THE_DUEL_WILL_BEGIN_IN_A_FEW_MOMENTS);
                msg2.addString(player.getName());
            }

            player.send(msg1);
            requestor.send(msg2);

            DuelManager.getInstance().addDuel(requestor, player, _partyDuel);
        } else if (_response == -1) {
            SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_IS_SET_TO_REFUSE_DUEL_REQUEST);
            sm.addPcName(player);
            requestor.send(sm);
        } else {
            SystemMessage msg = null;
            if (_partyDuel == 1) {
                msg = SystemMessage.getSystemMessage(SystemMessageId.THE_OPPOSING_PARTY_HAS_DECLINED_YOUR_CHALLENGE_TO_A_DUEL);
            } else {
                msg = SystemMessage.getSystemMessage(SystemMessageId.C1_HAS_DECLINED_YOUR_CHALLENGE_TO_A_DUEL);
                msg.addPcName(player);
            }
            requestor.send(msg);
        }

        player.setActiveRequester(null);
        requestor.onTransactionResponse();
    }
    */
    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}
