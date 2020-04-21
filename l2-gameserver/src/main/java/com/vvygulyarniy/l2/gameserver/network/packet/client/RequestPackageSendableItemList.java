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

import java.nio.ByteBuffer;

/**
 * @author -Wooden-
 * @author UnAfraid Thanks mrTJO
 */
public class RequestPackageSendableItemList extends L2GameClientPacket {

    private static final String _C_A7_REQUESTPACKAGESENDABLEITEMLIST = "[C] A7 RequestPackageSendableItemList";
    private int _objectID;

    public RequestPackageSendableItemList(ByteBuffer buf) {
        super(buf);
    }

    @Override
    protected void readImpl() {
        _objectID = readD();
    }

   /* @Override
    public void runImpl() {
        L2ItemInstance[] items = getClient().getActiveChar().getInventory().getAvailableItems(true, true, true);
        sendPacket(new PackageSendableList(items, _objectID));
    }*/

    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new RuntimeException("Not implemented yet");
    }
}
