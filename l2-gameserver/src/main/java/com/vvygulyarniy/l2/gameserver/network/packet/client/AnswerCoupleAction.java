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

/**
 * @author JIV
 */
public class AnswerCoupleAction extends L2GameClientPacket {
    private static final String _C__D0_7A_ANSWERCOUPLEACTION = "[C] D0:7A AnswerCoupleAction";

    private int _charObjId;
    private int _actionId;
    private int _answer;

    @Override
    protected void readImpl() {
        _actionId = readD();
        _answer = readD();
        _charObjId = readD();
    }
/*

    @Override
    protected void runImpl() {
        L2PcInstance activeChar = getActiveChar();
        L2PcInstance target = L2World.getInstance().getPlayer(_charObjId);
        if ((activeChar == null) || (target == null)) {
            return;
        }
        if ((target.getMultiSocialTarget() != activeChar.getObjectId()) || (target.getMultiSociaAction() != _actionId)) {
            return;
        }
        if (_answer == 0) // cancel
        {
            target.send(SystemMessageId.COUPLE_ACTION_DENIED);
        } else if (_answer == 1) // approve
        {
            final int distance = (int) activeChar.calculateDistance(target, false, false);
            if ((distance > 125) || (distance < 15) || (activeChar.getObjectId() == target.getObjectId())) {
                send(SystemMessageId.TARGET_DO_NOT_MEET_LOC_REQUIREMENTS);
                target.send(SystemMessageId.TARGET_DO_NOT_MEET_LOC_REQUIREMENTS);
                return;
            }
            int heading = Util.calculateHeadingFrom(activeChar, target);
            activeChar.broadcastPacket(new ExRotation(activeChar.getObjectId(), heading));
            activeChar.setHeading(heading);
            heading = Util.calculateHeadingFrom(target, activeChar);
            target.setHeading(heading);
            target.broadcastPacket(new ExRotation(target.getObjectId(), heading));
            activeChar.broadcastPacket(new SocialAction(activeChar.getObjectId(), _actionId));
            target.broadcastPacket(new SocialAction(_charObjId, _actionId));
        } else if (_answer == -1) // refused
        {
            SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_IS_SET_TO_REFUSE_COUPLE_ACTIONS);
            sm.addPcName(activeChar);
            target.send(sm);
        }
        target.setMultiSocialAction(0, 0);
    }

*/

    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}