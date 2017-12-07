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
import lombok.ToString;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.nio.ByteBuffer;

/**
 * format: ddddddd X:%d Y:%d Z:%d OriginX:%d OriginY:%d OriginZ:%d
 *
 * @author GodKratos
 */
@ToString
public class MoveToLocationInAirShip extends L2GameClientPacket {
    private static final String _C__D0_20_MOVETOLOCATIONINAIRSHIP = "[C] D0:20 MoveToLocationInAirShip";

    private int _shipId;
    private int _targetX;
    private int _targetY;
    private int _targetZ;
    private int _originX;
    private int _originY;
    private int _originZ;

    public MoveToLocationInAirShip(ByteBuffer buf) {
        super(buf);
    }

    @Override
    protected void readImpl() {
        _shipId = readD();
        _targetX = readD();
        _targetY = readD();
        _targetZ = readD();
        _originX = readD();
        _originY = readD();
        _originZ = readD();
    }

/*
    @Override
    protected void runImpl() {
        final L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }

        if ((_targetX == _originX) && (_targetY == _originY) && (_targetZ == _originZ)) {
            activeChar.send(new StopMoveInVehicle(activeChar, _shipId));
            return;
        }

        if (activeChar.isAttackingNow() && (activeChar.getActiveWeaponItem() != null) && (activeChar.getActiveWeaponItem().getItemType() == WeaponType.BOW)) {
            activeChar.send(ActionFailed.STATIC_PACKET);
            return;
        }

        if (activeChar.isSitting() || activeChar.isMovementDisabled()) {
            activeChar.send(ActionFailed.STATIC_PACKET);
            return;
        }

        if (!activeChar.isInAirShip()) {
            activeChar.send(ActionFailed.STATIC_PACKET);
            return;
        }

        final L2AirShipInstance airShip = activeChar.getAirShip();
        if (airShip.getObjectId() != _shipId) {
            activeChar.send(ActionFailed.STATIC_PACKET);
            return;
        }

        activeChar.setInVehiclePosition(new Location(_targetX, _targetY, _targetZ));
        activeChar.broadcastPacket(new ExMoveToLocationInAirShip(activeChar));
    }
*/


    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}
