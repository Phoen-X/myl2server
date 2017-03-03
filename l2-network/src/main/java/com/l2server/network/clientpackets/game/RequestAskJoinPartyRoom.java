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
 * Format: (ch) S
 *
 * @author -Wooden-, Tryskell
 */
public class RequestAskJoinPartyRoom extends L2GameClientPacket {
    private String _name;

    @Override
    protected void readImpl() {
        _name = readS();
    }

    /*@Override
    protected void runImpl() {
        final L2PcInstance player = getActiveChar();
        if (player == null) {
            return;
        }

        // Send PartyRoom invite request (with activeChar) name to the target
        final L2PcInstance target = L2World.getInstance().getPlayer(_name);
        if (target != null) {
            if (!target.isProcessingRequest()) {
                player.onTransactionRequest(target);
                target.send(new ExAskJoinPartyRoom(player.getName()));
            } else {
                player.send(SystemMessage.getSystemMessage(SystemMessageId.C1_IS_BUSY_TRY_LATER).addPcName(target));
            }
        } else {
            player.send(SystemMessageId.TARGET_IS_NOT_FOUND_IN_THE_GAME);
        }
    }*/

    @Override
    public void process(GameServerPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}