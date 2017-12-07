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

public final class RequestAnswerFriendInvite extends L2GameClientPacket {

    private int _response;

    public RequestAnswerFriendInvite(ByteBuffer buf) {
        super(buf);
    }

    @Override
    protected void readImpl() {
        _response = readD();
    }

    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }

/*
    @Override
    protected void runImpl() {
        final L2PcInstance player = getActiveChar();
        if (player == null) {
            return;
        }

        final L2PcInstance requestor = player.getActiveRequester();
        if (requestor == null) {
            return;
        }

        if (player.getFriendList().contains(requestor.getObjectId()) //
                || requestor.getFriendList().contains(player.getObjectId())) {
            final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_ALREADY_IN_FRIENDS_LIST);
            sm.addCharName(player);
            requestor.send(sm);
            return;
        }

        if (_response == 1) {
            try (Connection con = ConnectionFactory.getInstance().getConnection();
                 PreparedStatement statement = con.prepareStatement("INSERT INTO character_friends (charId, friendId) VALUES (?, ?), (?, ?)")) {
                statement.setInt(1, requestor.getObjectId());
                statement.setInt(2, player.getObjectId());
                statement.setInt(3, player.getObjectId());
                statement.setInt(4, requestor.getObjectId());
                statement.execute();
                SystemMessage msg = SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_SUCCEEDED_INVITING_FRIEND);
                requestor.send(msg);

                // Player added to your friend list
                msg = SystemMessage.getSystemMessage(SystemMessageId.S1_ADDED_TO_FRIENDS);
                msg.addString(player.getName());
                requestor.send(msg);
                requestor.getFriendList().add(player.getObjectId());

                // has joined as friend.
                msg = SystemMessage.getSystemMessage(SystemMessageId.S1_JOINED_AS_FRIEND);
                msg.addString(requestor.getName());
                player.send(msg);
                player.getFriendList().add(requestor.getObjectId());

                // Send notifications for both player in order to show them online
                player.send(new FriendPacket(true, requestor.getObjectId()));
                requestor.send(new FriendPacket(true, player.getObjectId()));
            } catch (Exception e) {
                _log.log(Level.WARNING, "Could not add friend objectid: " + e.getMessage(), e);
            }
        } else {
            SystemMessage msg = SystemMessage.getSystemMessage(SystemMessageId.FAILED_TO_INVITE_A_FRIEND);
            requestor.send(msg);
        }

        player.setActiveRequester(null);
        requestor.onTransactionResponse();
    }
*/

}
