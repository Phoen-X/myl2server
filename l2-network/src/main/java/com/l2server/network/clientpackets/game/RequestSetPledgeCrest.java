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
 * Client packet for setting/deleting clan crest.
 */
public final class RequestSetPledgeCrest extends L2GameClientPacket {
    private static final String _C__09_REQUESTSETPLEDGECREST = "[C] 09 RequestSetPledgeCrest";

    private int _length;
    private byte[] _data = null;

    @Override
    protected void readImpl() {
        _length = readD();
        if (_length > 256) {
            return;
        }

        _data = new byte[_length];
        readB(_data);
    }

    /*@Override
    protected void runImpl() {
        final L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }

        if ((_length < 0)) {
            activeChar.send(SystemMessageId.WRONG_SIZE_UPLOADED_CREST);
            return;
        }

        if (_length > 256) {
            activeChar.send(SystemMessageId.THE_SIZE_OF_THE_IMAGE_FILE_IS_INAPPROPRIATE);
            return;
        }

        final L2Clan clan = activeChar.getClan();
        if (clan == null) {
            return;
        }

        if (clan.getDissolvingExpiryTime() > System.currentTimeMillis()) {
            activeChar.send(SystemMessageId.CANNOT_SET_CREST_WHILE_DISSOLUTION_IN_PROGRESS);
            return;
        }

        if (!activeChar.hasClanPrivilege(ClanPrivilege.CL_REGISTER_CREST)) {
            activeChar.send(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
            return;
        }

        if (_length == 0) {
            if (clan.getCrestId() != 0) {
                clan.changeClanCrest(0);
                activeChar.send(SystemMessageId.CLAN_CREST_HAS_BEEN_DELETED);
            }
        } else {
            if (clan.getLevel() < 3) {
                activeChar.send(SystemMessageId.CLAN_LVL_3_NEEDED_TO_SET_CREST);
                return;
            }

            final L2Crest crest = CrestTable.getInstance().createCrest(_data, CrestType.PLEDGE);
            if (crest != null) {
                clan.changeClanCrest(crest.getId());
                activeChar.send(SystemMessageId.CLAN_CREST_WAS_SUCCESSFULLY_REGISTRED);
            }
        }
    }*/
    @Override
    public void process(GameServerPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}
