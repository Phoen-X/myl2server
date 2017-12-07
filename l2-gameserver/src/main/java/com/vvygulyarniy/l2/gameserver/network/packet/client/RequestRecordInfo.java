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

public class RequestRecordInfo extends L2GameClientPacket {
    private static final String _C__6E_REQUEST_RECORD_INFO = "[C] 6E RequestRecordInfo";

    public RequestRecordInfo(
            ByteBuffer buf) {
        super(buf);
    }

    @Override
    protected void readImpl() {
        // trigger
    }

    /*@Override
    protected void runImpl() {
        final L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }

        activeChar.send(new UserInfo(activeChar));
        activeChar.send(new ExBrExtraUserInfo(activeChar));

        Collection<L2Object> objs = activeChar.getKnownList().getKnownObjects().values();
        for (L2Object object : objs) {
            if (object.getPoly().isMorphed() && object.getPoly().getPolyType().equals("item")) {
                activeChar.send(new SpawnItem(object));
            } else {
                if (!object.isVisibleFor(activeChar)) {
                    object.sendInfo(activeChar);

                    if (object instanceof L2Character) {
                        // Update the state of the L2Character object client
                        // side by sending Server->Client packet
                        // MoveToPawn/CharMoveToLocation and AutoAttackStart to
                        // the L2PcInstance
                        final L2Character obj = (L2Character) object;
                        if (obj.getAI() != null) {
                            obj.getAI().describeStateToPlayer(activeChar);
                        }
                    }
                }
            }
        }
    }
    */

    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}
