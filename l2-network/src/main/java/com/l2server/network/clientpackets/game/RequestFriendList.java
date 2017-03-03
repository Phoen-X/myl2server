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

/**
 * This class ...
 *
 * @version $Revision: 1.3.4.3 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestFriendList extends L2GameClientPacket {
    private static final String _C__79_REQUESTFRIENDLIST = "[C] 79 RequestFriendList";

    @Override
    protected void readImpl() {
        // trigger
    }

/*
    @Override
    protected void runImpl() {
        L2PcInstance activeChar = getClient().getActiveChar();

        if (activeChar == null) {
            return;
        }

        SystemMessage sm;

        // ======<Friend List>======
        activeChar.send(SystemMessageId.FRIEND_LIST_HEADER);

        L2PcInstance friend = null;
        for (int id : activeChar.getFriendList()) {
            // int friendId = rset.getInt("friendId");
            String friendName = CharNameTable.getInstance().getNameById(id);

            if (friendName == null) {
                continue;
            }

            friend = L2World.getInstance().getPlayer(friendName);

            if ((friend == null) || !friend.isOnline()) {
                // (Currently: Offline)
                sm = SystemMessage.getSystemMessage(SystemMessageId.S1_OFFLINE);
                sm.addString(friendName);
            } else {
                // (Currently: Online)
                sm = SystemMessage.getSystemMessage(SystemMessageId.S1_ONLINE);
                sm.addString(friendName);
            }

            activeChar.send(sm);
        }

        // =========================
        activeChar.send(SystemMessageId.FRIEND_LIST_FOOTER);
    }
*/


    @Override
    public void process(GameServerPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}
