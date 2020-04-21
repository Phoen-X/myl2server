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

public final class RequestAnswerJoinParty extends L2GameClientPacket {
    private static final String _C__43_REQUESTANSWERPARTY = "[C] 43 RequestAnswerJoinParty";

    private int _response;

    public RequestAnswerJoinParty(ByteBuffer buf) {
        super(buf);
    }

    @Override
    protected void readImpl() {
        _response = readD();
    }

    /*@Override
    protected void runImpl() {
        final L2PcInstance player = getClient().getActiveChar();
        if (player == null) {
            return;
        }

        final L2PcInstance requestor = player.getActiveRequester();
        if (requestor == null) {
            return;
        }

        requestor.send(new JoinParty(_response));

        switch (_response) {
            case -1: // Party disable by player client config
            {
                SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_IS_SET_TO_REFUSE_PARTY_REQUEST);
                sm.addPcName(player);
                requestor.send(sm);
                break;
            }
            case 0: // Party cancel by player
            {

                // requestor.send(SystemMessageId.PLAYER_DECLINED); FIXME: Done in client?
                break;
            }
            case 1: // Party accept by player
            {
                if (requestor.isInParty()) {
                    if (requestor.getParty().getMemberCount() >= 9) {
                        SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.PARTY_FULL);
                        player.send(sm);
                        requestor.send(sm);
                        return;
                    }
                    player.joinParty(requestor.getParty());
                } else {
                    requestor.setParty(new L2Party(requestor, requestor.getPartyDistributionType()));
                    player.joinParty(requestor.getParty());
                }

                if (requestor.isInPartyMatchRoom() && player.isInPartyMatchRoom()) {
                    final PartyMatchRoomList list = PartyMatchRoomList.getInstance();
                    if ((list != null) && (list.getPlayerRoomId(requestor) == list.getPlayerRoomId(player))) {
                        final PartyMatchRoom room = list.getPlayerRoom(requestor);
                        if (room != null) {
                            final ExManagePartyRoomMember packet = new ExManagePartyRoomMember(player, room, 1);
                            for (L2PcInstance member : room.getPartyMembers()) {
                                if (member != null) {
                                    member.send(packet);
                                }
                            }
                        }
                    }
                } else if (requestor.isInPartyMatchRoom() && !player.isInPartyMatchRoom()) {
                    final PartyMatchRoomList list = PartyMatchRoomList.getInstance();
                    if (list != null) {
                        final PartyMatchRoom room = list.getPlayerRoom(requestor);
                        if (room != null) {
                            room.addMember(player);
                            ExManagePartyRoomMember packet = new ExManagePartyRoomMember(player, room, 1);
                            for (L2PcInstance member : room.getPartyMembers()) {
                                if (member != null) {
                                    member.send(packet);
                                }
                            }
                            player.setPartyRoom(room.getId());
                            // player.setPartyMatching(1);
                            player.broadcastUserInfo();
                        }
                    }
                }
                break;
            }
        }

        if (requestor.isInParty()) {
            requestor.getParty().setPendingInvitation(false); // if party is null, there is no need of decreasing
        }

        player.setActiveRequester(null);
        requestor.onTransactionResponse();
    }
    */
    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new RuntimeException("Not implemented yet");
    }
}
