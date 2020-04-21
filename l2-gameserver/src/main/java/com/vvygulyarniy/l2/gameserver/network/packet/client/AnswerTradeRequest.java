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
 * @version $Revision: 1.5.4.2 $ $Date: 2005/03/27 15:29:30 $
 */
public final class AnswerTradeRequest extends L2GameClientPacket {
    private static final String _C__55_ANSWERTRADEREQUEST = "[C] 55 AnswerTradeRequest";

    private int _response;

    public AnswerTradeRequest(ByteBuffer buf) {
        super(buf);
    }

    @Override
    protected void readImpl() {
        _response = readD();
    }

    /*@Override
    protected void runImpl() {
        L2PcInstance player = getClient().getActiveChar();
        if (player == null) {
            return;
        }

        if (!player.getAccessLevel().allowTransaction()) {
            player.send(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
            send(ActionFailed.STATIC_PACKET);
            return;
        }

        L2PcInstance partner = player.getActiveRequester();
        if (partner == null) {
            // Trade partner not found, cancel trade
            player.send(new TradeDone(0));
            player.send(SystemMessage.getSystemMessage(SystemMessageId.TARGET_IS_NOT_FOUND_IN_THE_GAME));
            player.setActiveRequester(null);
            return;
        } else if (L2World.getInstance().getPlayer(partner.getObjectId()) == null) {
            // Trade partner not found, cancel trade
            player.send(new TradeDone(0));
            player.send(SystemMessage.getSystemMessage(SystemMessageId.TARGET_IS_NOT_FOUND_IN_THE_GAME));
            player.setActiveRequester(null);
            return;
        }

        if ((_response == 1) && !partner.isRequestExpired()) {
            player.startTrade(partner);
        } else {
            SystemMessage msg = SystemMessage.getSystemMessage(SystemMessageId.C1_DENIED_TRADE_REQUEST);
            msg.addString(player.getName());
            partner.send(msg);
        }

        // Clears requesting status
        player.setActiveRequester(null);

        partner.onTransactionResponse();
    }
    */
    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new RuntimeException("Not implemented yet");
    }
}
