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
import org.jetbrains.annotations.NotNull;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.nio.ByteBuffer;

public final class RequestVoteNew extends L2GameClientPacket {
    private static final String _C__D0_7E_REQUESTVOTENEW = "[C] D0:7E RequestVoteNew";

    private int _targetId;

    public RequestVoteNew(@NotNull ByteBuffer buffer) {
        super(buffer);
    }

    @Override
    protected void readImpl() {
        _targetId = readD();
    }

/*
    @Override
    protected void runImpl() {
        L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }

        L2Object object = activeChar.getTarget();

        if (!(object instanceof L2PcInstance)) {
            if (object == null) {
                activeChar.send(SystemMessageId.SELECT_TARGET);
            } else {
                activeChar.send(SystemMessageId.TARGET_IS_INCORRECT);
            }
            return;
        }

        L2PcInstance target = (L2PcInstance) object;

        if (target.getObjectId() != _targetId) {
            return;
        }

        if (target == activeChar) {
            activeChar.send(SystemMessageId.YOU_CANNOT_RECOMMEND_YOURSELF);
            return;
        }

        if (activeChar.getRecomLeft() <= 0) {
            activeChar.send(SystemMessageId.YOU_CURRENTLY_DO_NOT_HAVE_ANY_RECOMMENDATIONS);
            return;
        }

        if (target.getRecomHave() >= 255) {
            activeChar.send(SystemMessageId.YOUR_TARGET_NO_LONGER_RECEIVE_A_RECOMMENDATION);
            return;
        }

        activeChar.giveRecom(target);

        SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_RECOMMENDED_C1_YOU_HAVE_S2_RECOMMENDATIONS_LEFT);
        sm.addPcName(target);
        sm.addInt(activeChar.getRecomLeft());
        activeChar.send(sm);

        sm = SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_BEEN_RECOMMENDED_BY_C1);
        sm.addPcName(activeChar);
        target.send(sm);

        activeChar.send(new UserInfo(activeChar));
        send(new ExBrExtraUserInfo(activeChar));
        target.broadcastUserInfo();

        activeChar.send(new ExVoteSystemInfo(activeChar));
        target.send(new ExVoteSystemInfo(target));
    }
*/


    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}
