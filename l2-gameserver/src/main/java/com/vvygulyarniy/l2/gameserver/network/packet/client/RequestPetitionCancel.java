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
 * Format: (c) d
 * <ul>
 * <li>d: Unknown</li>
 * </ul>
 * </p>
 *
 * @author -Wooden-, TempyIncursion
 */
public final class RequestPetitionCancel extends L2GameClientPacket {
    private static final String _C__8A_REQUEST_PETITIONCANCEL = "[C] 8A RequestPetitionCancel";

    // private int _unknown;

    @Override
    protected void readImpl() {
        // _unknown = readD(); This is pretty much a trigger packet.
    }

    /*@Override
    protected void runImpl() {
        L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }

        if (PetitionManager.getInstance().isPlayerInConsultation(activeChar)) {
            if (activeChar.isGM()) {
                PetitionManager.getInstance().endActivePetition(activeChar);
            } else {
                activeChar.send(SystemMessageId.PETITION_UNDER_PROCESS);
            }
        } else {
            if (PetitionManager.getInstance().isPlayerPetitionPending(activeChar)) {
                if (PetitionManager.getInstance().cancelActivePetition(activeChar)) {
                    int numRemaining = Config.MAX_PETITIONS_PER_PLAYER - PetitionManager.getInstance().getPlayerTotalPetitionCount(activeChar);

                    SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.PETITION_CANCELED_SUBMIT_S1_MORE_TODAY);
                    sm.addString(String.valueOf(numRemaining));
                    activeChar.send(sm);

                    // Notify all GMs that the player's pending petition has been cancelled.
                    String msgContent = activeChar.getName() + " has canceled a pending petition.";
                    AdminData.getInstance().broadcastToGMs(new CreatureSay(activeChar.getObjectId(), Say2.HERO_VOICE, "Petition System", msgContent));
                } else {
                    activeChar.send(SystemMessageId.FAILED_CANCEL_PETITION_TRY_LATER);
                }
            } else {
                activeChar.send(SystemMessageId.PETITION_NOT_SUBMITTED);
            }
        }
    }
    */
    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}