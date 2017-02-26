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
import com.vvygulyarniy.l2.domain.geo.Position;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * This class ...
 *
 * @version $Revision: 1.1.4.3 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestGetOnVehicle extends L2GameClientPacket {
    private static final String _C__53_GETONVEHICLE = "[C] 53 GetOnVehicle";

    private int _boatId;
    private Position _pos;

    @Override
    protected void readImpl() {
        int x, y, z;
        _boatId = readD();
        x = readD();
        y = readD();
        z = readD();
        _pos = new Position(x, y, z);
    }

    /*@Override
    protected void runImpl() {
        final L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }

        L2BoatInstance boat;
        if (activeChar.isInBoat()) {
            boat = activeChar.getBoat();
            if (boat.getObjectId() != _boatId) {
                sendPacket(ActionFailed.STATIC_PACKET);
                return;
            }
        } else {
            boat = BoatManager.getInstance().getBoat(_boatId);
            if ((boat == null) || boat.isMoving() || !activeChar.isInsideRadius(boat, 1000, true, false)) {
                sendPacket(ActionFailed.STATIC_PACKET);
                return;
            }
        }

        activeChar.setInVehiclePosition(_pos);
        activeChar.setVehicle(boat);
        activeChar.broadcastPacket(new GetOnVehicle(activeChar.getObjectId(), boat.getObjectId(), _pos));

        activeChar.setXYZ(boat.getX(), boat.getY(), boat.getZ());
        activeChar.setInsideZone(ZoneId.PEACE, true);
        activeChar.revalidateZone(true);
    }*/


    @Override
    public void process(GameServerPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}
