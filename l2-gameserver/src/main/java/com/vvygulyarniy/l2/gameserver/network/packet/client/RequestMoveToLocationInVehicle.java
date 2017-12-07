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

@ToString
public final class RequestMoveToLocationInVehicle extends L2GameClientPacket {
    private static final String _C__75_MOVETOLOCATIONINVEHICLE = "[C] 75 RequestMoveToLocationInVehicle";

    private int _boatId;
    private int _targetX;
    private int _targetY;
    private int _targetZ;
    private int _originX;
    private int _originY;
    private int _originZ;

    public RequestMoveToLocationInVehicle(ByteBuffer buf) {
        super(buf);
    }

    @Override
    protected void readImpl() {
        _boatId = readD(); // objectId of boat
        _targetX = readD();
        _targetY = readD();
        _targetZ = readD();
        _originX = readD();
        _originY = readD();
        _originZ = readD();
    }

    /*@Override
    protected void runImpl() {
        final L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }

        if ((Config.PLAYER_MOVEMENT_BLOCK_TIME > 0) && !activeChar.isGM() && (activeChar.getNotMoveUntil() > System.currentTimeMillis())) {
            activeChar.send(SystemMessageId.CANNOT_MOVE_WHILE_SPEAKING_TO_AN_NPC);
            activeChar.send(ActionFailed.STATIC_PACKET);
            return;
        }

        if ((_targetX == _originX) && (_targetY == _originY) && (_targetZ == _originZ)) {
            activeChar.send(new StopMoveInVehicle(activeChar, _boatId));
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

        if (activeChar.hasSummon()) {
            activeChar.send(SystemMessageId.RELEASE_PET_ON_BOAT);
            activeChar.send(ActionFailed.STATIC_PACKET);
            return;
        }

        if (activeChar.isTransformed()) {
            activeChar.send(SystemMessageId.CANT_POLYMORPH_ON_BOAT);
            activeChar.send(ActionFailed.STATIC_PACKET);
            return;
        }

        final L2BoatInstance boat;
        if (activeChar.isInBoat()) {
            boat = activeChar.getBoat();
            if (boat.getObjectId() != _boatId) {
                activeChar.send(ActionFailed.STATIC_PACKET);
                return;
            }
        } else {
            boat = BoatManager.getInstance().getBoat(_boatId);
            if ((boat == null) || !boat.isInsideRadius(activeChar, 300, true, false)) {
                activeChar.send(ActionFailed.STATIC_PACKET);
                return;
            }
            activeChar.setVehicle(boat);
        }

        final Location pos = new Location(_targetX, _targetY, _targetZ);
        final Location originPos = new Location(_originX, _originY, _originZ);
        activeChar.setInVehiclePosition(pos);
        activeChar.broadcastPacket(new MoveToLocationInVehicle(activeChar, pos, originPos));
    }
    */
    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}