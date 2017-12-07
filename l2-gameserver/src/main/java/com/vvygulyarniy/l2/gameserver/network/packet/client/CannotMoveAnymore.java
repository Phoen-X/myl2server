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
 * This class ...
 *
 * @version $Revision: 1.1.2.1.2.4 $ $Date: 2005/03/27 15:29:30 $
 */
public final class CannotMoveAnymore extends L2GameClientPacket {
    private static final String _C__47_STOPMOVE = "[C] 47 CannotMoveAnymore";

    private int _x;
    private int _y;
    private int _z;
    private int _heading;

    public CannotMoveAnymore(ByteBuffer buf) {
        super(buf);
    }

    @Override
    protected void readImpl() {
        _x = readD();
        _y = readD();
        _z = readD();
        _heading = readD();
    }

    /*@Override
    protected void runImpl() {
        L2PcInstance player = getClient().getActiveChar();
        if (player == null) {
            return;
        }

        if (Config.DEBUG) {
            _log.fine("client: x:" + _x + " y:" + _y + " z:" + _z + " server x:" + player.getX() + " y:" + player.getY() + " z:" + player.getZ());
        }
        if (player.getAI() != null) {
            player.getAI().notifyEvent(CtrlEvent.EVT_ARRIVED_BLOCKED, new Location(_x, _y, _z, _heading));
        }
    }*/

    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}
