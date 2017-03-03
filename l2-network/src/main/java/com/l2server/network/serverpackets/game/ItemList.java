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
package com.l2server.network.serverpackets.game;

import com.vvygulyarniy.l2.domain.character.L2Character;
import lombok.ToString;

import java.nio.ByteBuffer;

@ToString
public final class ItemList extends L2GameServerPacket {
    private final L2Character activeChar;
    //private final List<L2ItemInstance> _items = new ArrayList<>();
    private final boolean _showWindow;

    public ItemList(L2Character activeChar, boolean showWindow) {
        this.activeChar = activeChar;
        _showWindow = showWindow;

        /*for (L2ItemInstance item : activeChar.getInventory().getItems()) {
            if (!item.isQuestItem()) {
                _items.add(item);
            }
        }*/
    }

    @Override
    protected final void writeImpl(final ByteBuffer buffer) {
        writeC(buffer, 0x11);
        writeH(buffer, _showWindow ? 0x01 : 0x00);
        writeH(buffer, 0); // _items.size()
        /*for (L2ItemInstance item : _items) {
            writeItem(item);
        }
        writeInventoryBlock(activeChar.getInventory());*/
    }

    /*@Override
    public void runImpl() {
        getClient().send(new ExQuestItemList(activeChar));
    }*/
}
