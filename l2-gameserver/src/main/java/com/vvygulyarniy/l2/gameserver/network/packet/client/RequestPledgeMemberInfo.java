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

/**
 * Format: (ch) dS
 *
 * @author -Wooden-
 */
public final class RequestPledgeMemberInfo extends L2GameClientPacket {
    private static final String _C__D0_16_REQUESTPLEDGEMEMBERINFO = "[C] D0:16 RequestPledgeMemberInfo";
    @SuppressWarnings("unused")
    private int _unk1;
    private String _player;

    public RequestPledgeMemberInfo(ByteBuffer buf) {
        super(buf);
    }

    @Override
    protected void readImpl() {
        _unk1 = readD();
        _player = readS();
    }

    /*@Override
    protected void runImpl() {
        // _log.info("C5: RequestPledgeMemberInfo d:"+_unk1);
        // _log.info("C5: RequestPledgeMemberInfo S:"+_player);
        L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }
        // do we need powers to do that??
        L2Clan clan = activeChar.getClan();
        if (clan == null) {
            return;
        }
        L2ClanMember member = clan.getClanMember(_player);
        if (member == null) {
            return;
        }
        activeChar.sendPacket(new PledgeReceiveMemberInfo(member));
    }*/

    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}