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

public class RequestTutorialLinkHtml extends L2GameClientPacket {
    private static final String _C__85_REQUESTTUTORIALLINKHTML = "[C] 85 RequestTutorialLinkHtml";

    private String _bypass;

    public RequestTutorialLinkHtml(ByteBuffer buf) {
        super(buf);
    }

    @Override
    protected void readImpl() {
        _bypass = readS();
    }

    /*@Override
    protected void runImpl() {
        final L2PcInstance player = getClient().getActiveChar();
        if (player == null) {
            return;
        }

        final IBypassHandler handler = BypassHandler.getInstance().getHandler(_bypass);
        if (handler != null) {
            handler.useBypass(_bypass, player, null);
        } else {
            L2ClassMasterInstance.onTutorialLink(player, _bypass);

            QuestState qs = player.getQuestState("255_Tutorial");
            if (qs != null) {
                qs.getQuest().notifyEvent(_bypass, null, player);
            }
        }
    }
    */
    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new RuntimeException("Not implemented yet");
    }
}
