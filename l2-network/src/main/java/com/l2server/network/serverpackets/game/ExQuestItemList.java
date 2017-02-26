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

import lombok.ToString;

import java.nio.ByteBuffer;

/**
 * @author JIV
 */
@ToString
public class ExQuestItemList extends L2GameServerPacket {
    /*private final List<L2ItemInstance> _items = new ArrayList<>();
    */
    public ExQuestItemList() {
        /*for (L2ItemInstance item : activeChar.getInventory().getItems())
        {
			if (item.isQuestItem())
			{
				_items.add(item);
			}
		}*/
    }


    @Override
    protected void writeImpl(ByteBuffer buffer) {
        writeC(buffer, 0xFE);
        writeH(buffer, 0xC6);
        writeH(buffer, 0); //_items.size()
        /*for (L2ItemInstance item : _items) {
            writeItem(item);
        }
        writeInventoryBlock(_activeChar.getInventory());*/
    }
}
