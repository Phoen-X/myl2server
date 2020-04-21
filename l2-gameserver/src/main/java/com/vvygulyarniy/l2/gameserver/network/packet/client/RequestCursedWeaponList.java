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
 * Format: (ch)
 *
 * @author -Wooden-
 */
public class RequestCursedWeaponList extends L2GameClientPacket {
    private static final String _C__D0_2A_REQUESTCURSEDWEAPONLIST = "[C] D0:2A RequestCursedWeaponList";

    public RequestCursedWeaponList(
            ByteBuffer buf) {
        super(buf);
    }

    @Override
    protected void readImpl() {
        // nothing to read it's just a trigger
    }

    /*@Override
    protected void runImpl() {
        L2Character activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }

        // send a ExCursedWeaponList :p
        List<Integer> list = new ArrayList<>();
        for (int id : CursedWeaponsManager.getInstance().getCursedWeaponsIds()) {
            list.add(id);
        }

        activeChar.sendPacket(new ExCursedWeaponList(list));
    }*/

    @Override
    protected boolean triggersOnActionRequest() {
        return false;
    }

    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new RuntimeException("Not implemented yet");
    }
}
