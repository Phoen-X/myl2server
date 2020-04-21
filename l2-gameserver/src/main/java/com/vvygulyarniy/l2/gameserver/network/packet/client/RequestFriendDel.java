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
public final class RequestFriendDel extends L2GameClientPacket {

    private static final String _C__7A_REQUESTFRIENDDEL = "[C] 7A RequestFriendDel";

    private String _name;

    public RequestFriendDel(ByteBuffer buf) {
        super(buf);
    }

    @Override
    protected void readImpl() {
        _name = readS();
    }

    /*@Override
    protected void runImpl() {
        SystemMessage sm;

        L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }

        int id = CharNameTable.getInstance().getIdByName(_name);

        if (id == -1) {
            sm = SystemMessage.getSystemMessage(SystemMessageId.C1_NOT_ON_YOUR_FRIENDS_LIST);
            sm.addString(_name);
            activeChar.send(sm);
            return;
        }

        if (!activeChar.getFriendList().contains(id)) {
            sm = SystemMessage.getSystemMessage(SystemMessageId.C1_NOT_ON_YOUR_FRIENDS_LIST);
            sm.addString(_name);
            activeChar.send(sm);
            return;
        }

        try (Connection con = ConnectionFactory.getInstance().getConnection();
             PreparedStatement statement = con.prepareStatement("DELETE FROM character_friends WHERE (charId=? AND friendId=?) OR (charId=? AND friendId=?)")) {
            statement.setInt(1, activeChar.getObjectId());
            statement.setInt(2, id);
            statement.setInt(3, id);
            statement.setInt(4, activeChar.getObjectId());
            statement.execute();

            // Player deleted from your friend list
            sm = SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_BEEN_DELETED_FROM_YOUR_FRIENDS_LIST);
            sm.addString(_name);
            activeChar.send(sm);

            activeChar.getFriendList().remove(Integer.valueOf(id));
            activeChar.send(new FriendPacket(false, id));

            L2PcInstance player = L2World.getInstance().getPlayer(_name);
            if (player != null) {
                player.getFriendList().remove(Integer.valueOf(activeChar.getObjectId()));
                player.send(new FriendPacket(false, activeChar.getObjectId()));
            }
        } catch (Exception e) {
            _log.log(Level.WARNING, "could not del friend objectid: ", e);
        }
    }
    */
    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new RuntimeException("Not implemented yet");
    }
}
