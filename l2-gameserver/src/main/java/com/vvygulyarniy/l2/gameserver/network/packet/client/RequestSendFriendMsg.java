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

import java.util.logging.Logger;

/**
 * Recieve Private (Friend) Message - 0xCC Format: c SS S: Message S: Receiving Player
 *
 * @author Tempy
 */
public final class RequestSendFriendMsg extends L2GameClientPacket {
    private static final String _C__6B_REQUESTSENDMSG = "[C] 6B RequestSendFriendMsg";
    private static Logger _logChat = Logger.getLogger("chat");

    private String _message;
    private String _reciever;

    @Override
    protected void readImpl() {
        _message = readS();
        _reciever = readS();
    }

    /*@Override
    protected void runImpl() {
        final L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }

        if ((_message == null) || _message.isEmpty() || (_message.length() > 300)) {
            return;
        }

        final L2PcInstance targetPlayer = L2World.getInstance().getPlayer(_reciever);
        if ((targetPlayer == null) || !targetPlayer.getFriendList().contains(activeChar.getObjectId())) {
            activeChar.send(SystemMessageId.TARGET_IS_NOT_FOUND_IN_THE_GAME);
            return;
        }

        if (Config.LOG_CHAT) {
            LogRecord record = new LogRecord(Level.INFO, _message);
            record.setLoggerName("chat");
            record.setParameters(new Object[]
                    {
                            "PRIV_MSG",
                            "[" + activeChar.getName() + " to " + _reciever + "]"
                    });

            _logChat.log(record);
        }

        targetPlayer.send(new L2FriendSay(activeChar.getName(), _reciever, _message));
    }
    */
    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}