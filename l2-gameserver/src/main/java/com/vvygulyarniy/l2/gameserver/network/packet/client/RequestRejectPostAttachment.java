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

/**
 * @author Migi, DS
 */
public final class RequestRejectPostAttachment extends L2GameClientPacket {
    private static final String _C__D0_6B_REQUESTREJECTPOSTATTACHMENT = "[C] D0:6B RequestRejectPostAttachment";

    private int _msgId;

    public RequestRejectPostAttachment(@NotNull ByteBuffer buffer) {
        super(buffer);
    }

    @Override
    protected void readImpl() {
        _msgId = readD();
    }

    /*@Override
    public void runImpl() {
        if (!Config.ALLOW_MAIL || !Config.ALLOW_ATTACHMENTS) {
            return;
        }

        final L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }

        if (!getClient().getFloodProtectors().getTransaction().tryPerformAction("rejectattach")) {
            return;
        }

        if (!activeChar.isInsideZone(ZoneId.PEACE)) {
            activeChar.send(SystemMessageId.CANT_USE_MAIL_OUTSIDE_PEACE_ZONE);
            return;
        }

        Message msg = MailManager.getInstance().getMessage(_msgId);
        if (msg == null) {
            return;
        }

        if (msg.getReceiverId() != activeChar.getObjectId()) {
            Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " tried to reject not own attachment!", Config.DEFAULT_PUNISH);
            return;
        }

        if (!msg.hasAttachments() || (msg.getSendBySystem() != 0)) {
            return;
        }

        MailManager.getInstance().sendMessage(new Message(msg));

        activeChar.send(SystemMessageId.MAIL_SUCCESSFULLY_RETURNED);
        activeChar.send(new ExChangePostState(true, _msgId, Message.REJECTED));

        final L2PcInstance sender = L2World.getInstance().getPlayer(msg.getSenderId());
        if (sender != null) {
            SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_RETURNED_MAIL);
            sm.addCharName(activeChar);
            sender.send(sm);
        }
    }
    */
    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}
