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

public class RequestTutorialQuestionMark extends L2GameClientPacket {
    private static final String _C__87_REQUESTTUTORIALQUESTIONMARK = "[C] 87 RequestTutorialQuestionMark";

    private int _number = 0;

    public RequestTutorialQuestionMark(ByteBuffer buf) {
        super(buf);
    }

    @Override
    protected void readImpl() {
        _number = readD();
    }

/*
    @Override
    protected void runImpl() {
        L2PcInstance player = getClient().getActiveChar();

        if (player == null) {
            return;
        }

        L2ClassMasterInstance.onTutorialQuestionMark(player, _number);

        QuestState qs = player.getQuestState("255_Tutorial");
        if (qs != null) {
            qs.getQuest().notifyEvent("QM" + _number + "", null, player);
        }
    }

*/

    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}
