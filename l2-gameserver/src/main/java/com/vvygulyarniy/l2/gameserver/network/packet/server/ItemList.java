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
package com.vvygulyarniy.l2.gameserver.network.packet.server;

import com.vvygulyarniy.l2.gameserver.world.item.L2Item;
import lombok.ToString;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * Packet gives simple (non-quest) inventory list.
 */
@ToString
public final class ItemList extends L2GameServerPacket {
    private final List<L2Item> items;
    private final boolean _showWindow;

    public ItemList(final List<L2Item> items, boolean showWindow) {
        this.items = items;
        _showWindow = showWindow;
    }

    @Override
    protected final void writeImpl(final ByteBuffer buffer) {
        writeC(buffer, 0x11);
        writeH(buffer, _showWindow ? 0x01 : 0x00);
        writeH(buffer, items.size()); // items.size()
        /*for (L2ItemInstance item : items) {
            writeItem(item);
        }
        writeInventoryBlock(activeChar.getInventory());*/
    }

    /*@Override
    public void runImpl() {
        getClient().send(new ExQuestItemList(activeChar));
    }*/
}
