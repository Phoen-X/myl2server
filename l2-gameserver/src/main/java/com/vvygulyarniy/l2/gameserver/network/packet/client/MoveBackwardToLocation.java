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

import com.vvygulyarniy.l2.domain.geo.Position;
import com.vvygulyarniy.l2.gameserver.network.L2GameClient;
import com.vvygulyarniy.l2.gameserver.network.packet.L2ClientPacketProcessor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.nio.BufferUnderflowException;

/**
 * This class ...
 *
 * @version $Revision: 1.11.2.4.2.4 $ $Date: 2005/03/27 15:29:30 $
 */
@Slf4j
@ToString
@Getter
public class MoveBackwardToLocation extends L2GameClientPacket {
    // cdddddd
    private Position origin;
    private Position target;

    @SuppressWarnings("unused")
    private int moveMovement;

    @Override
    protected void readImpl() {
        this.target = new Position(readD(), readD(), readD());
        this.origin = new Position(readD(), readD(), readD());

        try {
            moveMovement = readD(); // is 0 if cursor keys are used 1 if mouse is used
        } catch (BufferUnderflowException e) {
            log.warn("Underflowed: {}", this);
        }
    }

    /*@Override
    protected void runImpl() {
        L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }

        if ((Config.PLAYER_MOVEMENT_BLOCK_TIME > 0) && !activeChar.isGM() && (activeChar.getNotMoveUntil() > System.currentTimeMillis())) {
            activeChar.send(SystemMessageId.CANNOT_MOVE_WHILE_SPEAKING_TO_AN_NPC);
            activeChar.send(ActionFailed.STATIC_PACKET);
            return;
        }

        if ((targetX == originX) && (targetY == originY) && (targetZ == originZ)) {
            activeChar.send(new StopMove(activeChar));
            return;
        }

        // Correcting targetZ from floor level to head level (?)
        // Client is giving floor level as targetZ but that floor level doesn't
        // match our current geodata and teleport coords as good as head level!
        // L2J uses floor, not head level as char coordinates. This is some
        // sort of incompatibility fix.
        // Validate position packets sends head level.
        targetZ += activeChar.getTemplate().getCollisionHeight();

        if (activeChar.getTeleMode() > 0) {
            if (activeChar.getTeleMode() == 1) {
                activeChar.setTeleMode(0);
            }
            activeChar.send(ActionFailed.STATIC_PACKET);
            activeChar.teleToLocation(new Location(targetX, targetY, targetZ));
            return;
        }

        double dx = targetX - activeChar.getX();
        double dy = targetY - activeChar.getY();
        // Can't move if character is confused, or trying to move a huge distance
        if (activeChar.isOutOfControl() || (((dx * dx) + (dy * dy)) > 98010000)) // 9900*9900
        {
            activeChar.send(ActionFailed.STATIC_PACKET);
            return;
        }

        activeChar.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new Location(targetX, targetY, targetZ));
    }*/
    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        processor.process(this, client);
    }
}
