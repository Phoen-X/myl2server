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

import java.nio.ByteBuffer;

/**
 * @author Migi, DS
 */
public final class RequestDeleteReceivedPost extends L2GameClientPacket {
    private static final String _C__D0_68_REQUESTDELETERECEIVEDPOST = "[C] D0:68 RequestDeleteReceivedPost";

    private static final int BATCH_LENGTH = 4; // length of the one item

    int[] _msgIds = null;

    public RequestDeleteReceivedPost(@NotNull ByteBuffer buffer) {
        super(buffer);
    }

    @Override
    protected void readImpl() {
        int count = readD();
        if ((count <= 0) || (count > 20) || ((count * BATCH_LENGTH) != getBuffer().remaining())) {
            return;
        }

        _msgIds = new int[count];
        for (int i = 0; i < count; i++) {
            _msgIds[i] = readD();
        }
    }

   /* @Override
    public void runImpl() {
        final L2PcInstance activeChar = getClient().getActiveChar();
        if ((activeChar == null) || (_msgIds == null) || !Config.ALLOW_MAIL) {
            return;
        }

        if (!activeChar.isInsideZone(ZoneId.PEACE)) {
            activeChar.send(SystemMessageId.CANT_USE_MAIL_OUTSIDE_PEACE_ZONE);
            return;
        }

        for (int msgId : _msgIds) {
            Message msg = MailManager.getInstance().getMessage(msgId);
            if (msg == null) {
                continue;
            }
            if (msg.getReceiverId() != activeChar.getObjectId()) {
                Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " tried to delete not own post!", Config.DEFAULT_PUNISH);
                return;
            }

            if (msg.hasAttachments() || msg.isDeletedByReceiver()) {
                return;
            }

            msg.setDeletedByReceiver();
        }
        activeChar.send(new ExChangePostState(true, _msgIds, Message.DELETED));
    }
*/

    @Override
    protected boolean triggersOnActionRequest() {
        return false;
    }

    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new RuntimeException("Not implemented yet");
    }
}