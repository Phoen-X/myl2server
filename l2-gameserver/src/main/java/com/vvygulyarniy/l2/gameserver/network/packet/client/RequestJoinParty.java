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
 * sample 29 42 00 00 10 01 00 00 00 format cdd
 *
 * @version $Revision: 1.7.4.4 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestJoinParty extends L2GameClientPacket {
    private static final String _C__42_REQUESTJOINPARTY = "[C] 42 RequestJoinParty";

    private String _name;
    private int _partyDistributionTypeId;

    @Override
    protected void readImpl() {
        _name = readS();
        _partyDistributionTypeId = readD();
    }

    /*@Override
    protected void runImpl() {
        L2PcInstance requestor = getClient().getActiveChar();
        L2PcInstance target = L2World.getInstance().getPlayer(_name);

        if (requestor == null) {
            return;
        }

        if (target == null) {
            requestor.send(SystemMessageId.FIRST_SELECT_USER_TO_INVITE_TO_PARTY);
            return;
        }

        if ((target.getClient() == null) || target.getClient().isDetached()) {
            requestor.sendMessage("Player is in offline mode.");
            return;
        }

        if (requestor.isPartyBanned()) {
            requestor.send(SystemMessageId.YOU_HAVE_BEEN_REPORTED_SO_PARTY_NOT_ALLOWED);
            requestor.send(ActionFailed.STATIC_PACKET);
            return;
        }

        if (target.isPartyBanned()) {
            SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_REPORTED_AND_CANNOT_PARTY);
            sm.addCharName(target);
            requestor.send(sm);
            return;
        }

        if (!target.isVisibleFor(requestor)) {
            requestor.send(SystemMessageId.TARGET_IS_INCORRECT);
            return;
        }

        SystemMessage sm;
        if (target.isInParty()) {
            sm = SystemMessage.getSystemMessage(SystemMessageId.C1_IS_ALREADY_IN_PARTY);
            sm.addString(target.getName());
            requestor.send(sm);
            return;
        }

        if (BlockList.isBlocked(target, requestor)) {
            sm = SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_ADDED_YOU_TO_IGNORE_LIST);
            sm.addCharName(target);
            requestor.send(sm);
            return;
        }

        if (target == requestor) {
            requestor.send(SystemMessageId.YOU_HAVE_INVITED_THE_WRONG_TARGET);
            return;
        }

        if (target.isCursedWeaponEquipped() || requestor.isCursedWeaponEquipped()) {
            requestor.send(SystemMessageId.INCORRECT_TARGET);
            return;
        }

        if (target.isJailed() || requestor.isJailed()) {
            requestor.sendMessage("You cannot invite a player while is in Jail.");
            return;
        }

        if (target.isInOlympiadMode() || requestor.isInOlympiadMode()) {
            if ((target.isInOlympiadMode() != requestor.isInOlympiadMode()) || (target.getOlympiadGameId() != requestor.getOlympiadGameId()) || (target.getOlympiadSide() != requestor.getOlympiadSide())) {
                requestor.send(SystemMessageId.A_USER_CURRENTLY_PARTICIPATING_IN_THE_OLYMPIAD_CANNOT_SEND_PARTY_AND_FRIEND_INVITATIONS);
                return;
            }
        }

        sm = SystemMessage.getSystemMessage(SystemMessageId.C1_INVITED_TO_PARTY);
        sm.addCharName(target);
        requestor.send(sm);

        if (!requestor.isInParty()) {
            createNewParty(target, requestor);
        } else {
            if (requestor.getParty().isInDimensionalRift()) {
                requestor.sendMessage("You cannot invite a player when you are in the Dimensional Rift.");
            } else {
                addTargetToParty(target, requestor);
            }
        }
    }

    private void addTargetToParty(L2PcInstance target, L2PcInstance requestor) {
        final L2Party party = requestor.getParty();
        // summary of ppl already in party and ppl that get invitation
        if (!party.isLeader(requestor)) {
            requestor.send(SystemMessageId.ONLY_LEADER_CAN_INVITE);
            return;
        }
        if (party.getMemberCount() >= 9) {
            requestor.send(SystemMessageId.PARTY_FULL);
            return;
        }
        if (party.getPendingInvitation() && !party.isInvitationRequestExpired()) {
            requestor.send(SystemMessageId.WAITING_FOR_ANOTHER_REPLY);
            return;
        }

        if (!target.isProcessingRequest()) {
            requestor.onTransactionRequest(target);
            // in case a leader change has happened, use party's mode
            target.send(new AskJoinParty(requestor.getName(), party.getDistributionType()));
            party.setPendingInvitation(true);

            if (Config.DEBUG) {
                _log.fine("sent out a party invitation to:" + target.getName());
            }

        } else {
            SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_IS_BUSY_TRY_LATER);
            sm.addString(target.getName());
            requestor.send(sm);

            if (Config.DEBUG) {
                _log.warning(requestor.getName() + " already received a party invitation");
            }
        }
    }
*/

/*
    private void createNewParty(L2PcInstance target, L2PcInstance requestor) {
        PartyDistributionType partyDistributionType = PartyDistributionType.findById(_partyDistributionTypeId);
        if (partyDistributionType == null) {
            return;
        }

        if (!target.isProcessingRequest()) {
            target.send(new AskJoinParty(requestor.getName(), partyDistributionType));
            target.setActiveRequester(requestor);
            requestor.onTransactionRequest(target);
            requestor.setPartyDistributionType(partyDistributionType);

            if (Config.DEBUG) {
                _log.fine("sent out a party invitation to:" + target.getName());
            }

        } else {
            requestor.send(SystemMessageId.WAITING_FOR_ANOTHER_REPLY);

            if (Config.DEBUG) {
                _log.warning(requestor.getName() + " already received a party invitation");
            }
        }
    }
*/

    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}
