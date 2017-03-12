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

/**
 * <p>
 * Format: (c) Sd
 * <ul>
 * <li>S: content</li>
 * <li>d: type</li>
 * </ul>
 * </p>
 *
 * @author -Wooden-, TempyIncursion
 */
public final class RequestPetition extends L2GameClientPacket {
    private static final String _C__89_RequestPetition = "[C] 89 RequestPetition";

    private String _content;
    private int _type; // 1 = on : 0 = off;

    @Override
    protected void readImpl() {
        _content = readS();
        _type = readD();
    }

/*
    @Override
    protected void runImpl() {
        L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }

        if (!AdminData.getInstance().isGmOnline(false)) {
            activeChar.send(SystemMessageId.NO_GM_PROVIDING_SERVICE_NOW);
            return;
        }

        if (!PetitionManager.getInstance().isPetitioningAllowed()) {
            activeChar.send(SystemMessageId.GAME_CLIENT_UNABLE_TO_CONNECT_TO_PETITION_SERVER);
            return;
        }

        if (PetitionManager.getInstance().isPlayerPetitionPending(activeChar)) {
            activeChar.send(SystemMessageId.ONLY_ONE_ACTIVE_PETITION_AT_TIME);
            return;
        }

        if (PetitionManager.getInstance().getPendingPetitionCount() == Config.MAX_PETITIONS_PENDING) {
            activeChar.send(SystemMessageId.PETITION_SYSTEM_CURRENT_UNAVAILABLE);
            return;
        }

        int totalPetitions = PetitionManager.getInstance().getPlayerTotalPetitionCount(activeChar) + 1;

        if (totalPetitions > Config.MAX_PETITIONS_PER_PLAYER) {
            SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.WE_HAVE_RECEIVED_S1_PETITIONS_TODAY);
            sm.addInt(totalPetitions);
            activeChar.send(sm);
            return;
        }

        if (_content.length() > 255) {
            activeChar.send(SystemMessageId.PETITION_MAX_CHARS_255);
            return;
        }

        int petitionId = PetitionManager.getInstance().submitPetition(activeChar, _content, _type);

        SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.PETITION_ACCEPTED_RECENT_NO_S1);
        sm.addInt(petitionId);
        activeChar.send(sm);

        sm = SystemMessage.getSystemMessage(SystemMessageId.SUBMITTED_YOU_S1_TH_PETITION_S2_LEFT);
        sm.addInt(totalPetitions);
        sm.addInt(Config.MAX_PETITIONS_PER_PLAYER - totalPetitions);
        activeChar.send(sm);

        sm = SystemMessage.getSystemMessage(SystemMessageId.S1_PETITION_ON_WAITING_LIST);
        sm.addInt(PetitionManager.getInstance().getPendingPetitionCount());
        activeChar.send(sm);
    }

*/

    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}
