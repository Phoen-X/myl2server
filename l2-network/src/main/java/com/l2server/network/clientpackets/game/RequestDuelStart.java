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
 * Format:(ch) Sd
 *
 * @author -Wooden-
 */
public final class RequestDuelStart extends L2GameClientPacket {
    private static final String _C__D0_1B_REQUESTDUELSTART = "[C] D0:1B RequestDuelStart";

    private String _player;
    private int _partyDuel;

    @Override
    protected void readImpl() {
        _player = readS();
        _partyDuel = readD();
    }

    /*@Override
    protected void runImpl() {
        L2PcInstance activeChar = getClient().getActiveChar();
        L2PcInstance targetChar = L2World.getInstance().getPlayer(_player);
        if (activeChar == null) {
            return;
        }
        if (targetChar == null) {
            activeChar.send(SystemMessageId.THERE_IS_NO_OPPONENT_TO_RECEIVE_YOUR_CHALLENGE_FOR_A_DUEL);
            return;
        }
        if (activeChar == targetChar) {
            activeChar.send(SystemMessageId.THERE_IS_NO_OPPONENT_TO_RECEIVE_YOUR_CHALLENGE_FOR_A_DUEL);
            return;
        }

        // Check if duel is possible
        if (!activeChar.canDuel()) {
            activeChar.send(SystemMessageId.YOU_ARE_UNABLE_TO_REQUEST_A_DUEL_AT_THIS_TIME);
            return;
        } else if (!targetChar.canDuel()) {
            activeChar.send(targetChar.getNoDuelReason());
            return;
        }
        // Players may not be too far apart
        else if (!activeChar.isInsideRadius(targetChar, 250, false, false)) {
            SystemMessage msg = SystemMessage.getSystemMessage(SystemMessageId.C1_CANNOT_RECEIVE_A_DUEL_CHALLENGE_BECAUSE_C1_IS_TOO_FAR_AWAY);
            msg.addString(targetChar.getName());
            activeChar.send(msg);
            return;
        }

        // Duel is a party duel
        if (_partyDuel == 1) {
            // Player must be in a party & the party leader
            if (!activeChar.isInParty() || !activeChar.getParty().isLeader(activeChar)) {
                activeChar.sendMessage("You have to be the leader of a party in order to request a party duel.");
                return;
            }
            // Target must be in a party
            else if (!targetChar.isInParty()) {
                activeChar.send(SystemMessageId.SINCE_THE_PERSON_YOU_CHALLENGED_IS_NOT_CURRENTLY_IN_A_PARTY_THEY_CANNOT_DUEL_AGAINST_YOUR_PARTY);
                return;
            }
            // Target may not be of the same party
            else if (activeChar.getParty().containsPlayer(targetChar)) {
                activeChar.sendMessage("This player is a member of your own party.");
                return;
            }

            // Check if every player is ready for a duel
            for (L2PcInstance temp : activeChar.getParty().getMembers()) {
                if (!temp.canDuel()) {
                    activeChar.sendMessage("Not all the members of your party are ready for a duel.");
                    return;
                }
            }
            L2PcInstance partyLeader = null; // snatch party leader of targetChar's party
            for (L2PcInstance temp : targetChar.getParty().getMembers()) {
                if (partyLeader == null) {
                    partyLeader = temp;
                }
                if (!temp.canDuel()) {
                    activeChar.send(SystemMessageId.THE_OPPOSING_PARTY_IS_CURRENTLY_UNABLE_TO_ACCEPT_A_CHALLENGE_TO_A_DUEL);
                    return;
                }
            }

            // Send request to targetChar's party leader
            if (partyLeader != null) {
                if (!partyLeader.isProcessingRequest()) {
                    activeChar.onTransactionRequest(partyLeader);
                    partyLeader.send(new ExDuelAskStart(activeChar.getName(), _partyDuel));

                    if (Config.DEBUG) {
                        _log.fine(activeChar.getName() + " requested a duel with " + partyLeader.getName());
                    }

                    SystemMessage msg = SystemMessage.getSystemMessage(SystemMessageId.C1_PARTY_HAS_BEEN_CHALLENGED_TO_A_DUEL);
                    msg.addString(partyLeader.getName());
                    activeChar.send(msg);

                    msg = SystemMessage.getSystemMessage(SystemMessageId.C1_PARTY_HAS_CHALLENGED_YOUR_PARTY_TO_A_DUEL);
                    msg.addString(activeChar.getName());
                    targetChar.send(msg);
                } else {
                    SystemMessage msg = SystemMessage.getSystemMessage(SystemMessageId.C1_IS_BUSY_TRY_LATER);
                    msg.addString(partyLeader.getName());
                    activeChar.send(msg);
                }
            }
        } else
        // 1vs1 duel
        {
            if (!targetChar.isProcessingRequest()) {
                activeChar.onTransactionRequest(targetChar);
                targetChar.send(new ExDuelAskStart(activeChar.getName(), _partyDuel));

                if (Config.DEBUG) {
                    _log.fine(activeChar.getName() + " requested a duel with " + targetChar.getName());
                }

                SystemMessage msg = SystemMessage.getSystemMessage(SystemMessageId.C1_HAS_BEEN_CHALLENGED_TO_A_DUEL);
                msg.addString(targetChar.getName());
                activeChar.send(msg);

                msg = SystemMessage.getSystemMessage(SystemMessageId.C1_HAS_CHALLENGED_YOU_TO_A_DUEL);
                msg.addString(activeChar.getName());
                targetChar.send(msg);
            } else {
                SystemMessage msg = SystemMessage.getSystemMessage(SystemMessageId.C1_IS_BUSY_TRY_LATER);
                msg.addString(targetChar.getName());
                activeChar.send(msg);
            }
        }
    }*/

    @Override
    public void process(GameServerPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}