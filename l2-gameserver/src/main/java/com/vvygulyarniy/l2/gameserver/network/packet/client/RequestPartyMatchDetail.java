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
 * @author Gnacik
 */
public final class RequestPartyMatchDetail extends L2GameClientPacket {
    private static final String _C__81_REQUESTPARTYMATCHDETAIL = "[C] 81 RequestPartyMatchDetail";

    private int _roomid;
    @SuppressWarnings("unused")
    private int _unk1;
    @SuppressWarnings("unused")
    private int _unk2;
    @SuppressWarnings("unused")
    private int _unk3;

    public RequestPartyMatchDetail(ByteBuffer buf) {
        super(buf);
    }

    @Override
    protected void readImpl() {
        _roomid = readD();
        // If player click on Room all unk are 0
        // If player click AutoJoin values are -1 1 1
        _unk1 = readD();
        _unk2 = readD();
        _unk3 = readD();
    }

    /*@Override
    protected void runImpl() {
        L2PcInstance _activeChar = getClient().getActiveChar();
        if (_activeChar == null) {
            return;
        }

        PartyMatchRoom _room = PartyMatchRoomList.getInstance().getRoom(_roomid);
        if (_room == null) {
            return;
        }

        if ((_activeChar.getLevel() >= _room.getMinLvl()) && (_activeChar.getLevel() <= _room.getMaxLvl())) {
            // Remove from waiting list
            PartyMatchWaitingList.getInstance().removePlayer(_activeChar);

            _activeChar.setPartyRoom(_roomid);

            _activeChar.send(new PartyMatchDetail(_activeChar, _room));
            _activeChar.send(new ExPartyRoomMember(_activeChar, _room, 0));

            for (L2PcInstance _member : _room.getPartyMembers()) {
                if (_member == null) {
                    continue;
                }

                _member.send(new ExManagePartyRoomMember(_activeChar, _room, 0));

                SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_ENTERED_PARTY_ROOM);
                sm.addCharName(_activeChar);
                _member.send(sm);
            }
            _room.addMember(_activeChar);

            // Info Broadcast
            _activeChar.broadcastUserInfo();
        } else {
            _activeChar.send(SystemMessageId.CANT_ENTER_PARTY_ROOM);
        }
    }
    */
    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new RuntimeException("Not implemented yet");
    }
}
