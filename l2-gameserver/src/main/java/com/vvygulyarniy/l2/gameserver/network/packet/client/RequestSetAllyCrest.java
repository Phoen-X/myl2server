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
 * Client packet for setting ally crest.
 */
public final class RequestSetAllyCrest extends L2GameClientPacket {
    private static final String _C__91_REQUESTSETALLYCREST = "[C] 91 RequestSetAllyCrest";

    private int _length;
    private byte[] _data = null;

    public RequestSetAllyCrest(ByteBuffer buf) {
        super(buf);
    }

    @Override
    protected void readImpl() {
        _length = readD();
        if (_length > 192) {
            return;
        }

        _data = new byte[_length];
        readB(_data);
    }

/*
    @Override
    protected void runImpl() {
        final L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }

        if (_length < 0) {
            activeChar.sendMessage("File transfer error.");
            return;
        }

        if (_length > 192) {
            activeChar.send(SystemMessageId.ADJUST_IMAGE_8_12);
            return;
        }

        if (activeChar.getAllyId() == 0) {
            activeChar.send(SystemMessageId.FEATURE_ONLY_FOR_ALLIANCE_LEADER);
            return;
        }

        final L2Clan leaderClan = ClanTable.getInstance().getClan(activeChar.getAllyId());

        if ((activeChar.getClanId() != leaderClan.getId()) || !activeChar.isClanLeader()) {
            activeChar.send(SystemMessageId.FEATURE_ONLY_FOR_ALLIANCE_LEADER);
            return;
        }

        if (_length == 0) {
            if (leaderClan.getAllyCrestId() != 0) {
                leaderClan.changeAllyCrest(0, false);
            }
        } else {
            final L2Crest crest = CrestTable.getInstance().createCrest(_data, CrestType.ALLY);
            if (crest != null) {
                leaderClan.changeAllyCrest(crest.getId(), false);
                activeChar.send(SystemMessageId.CLAN_CREST_WAS_SUCCESSFULLY_REGISTRED);
            }
        }

    }

*/

    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new RuntimeException("Not implemented yet");
    }
}
