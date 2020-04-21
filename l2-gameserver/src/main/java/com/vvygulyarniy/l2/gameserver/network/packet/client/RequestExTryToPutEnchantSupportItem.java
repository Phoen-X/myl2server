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
 * @author KenM
 */
public class RequestExTryToPutEnchantSupportItem extends L2GameClientPacket {
    private static final String _C__D0_4D_REQUESTEXTRYTOPUTENCHANTSUPPORTITEM = "[C] D0:4D RequestExTryToPutEnchantSupportItem";

    private int _supportObjectId;
    private int _enchantObjectId;

    public RequestExTryToPutEnchantSupportItem(@NotNull ByteBuffer buffer) {
        super(buffer);
    }

    @Override
    protected void readImpl() {
        _supportObjectId = readD();
        _enchantObjectId = readD();
    }

    /*@Override
    protected void runImpl() {
        final L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }

        if (activeChar.isEnchanting()) {
            final L2ItemInstance item = activeChar.getInventory().getItemByObjectId(_enchantObjectId);
            final L2ItemInstance scroll = activeChar.getInventory().getItemByObjectId(activeChar.getActiveEnchantItemId());
            final L2ItemInstance support = activeChar.getInventory().getItemByObjectId(_supportObjectId);

            if ((item == null) || (scroll == null) || (support == null)) {
                // message may be custom
                activeChar.send(SystemMessageId.INAPPROPRIATE_ENCHANT_CONDITION);
                activeChar.setActiveEnchantSupportItemId(L2PcInstance.ID_NONE);
                return;
            }

            final EnchantScroll scrollTemplate = EnchantItemData.getInstance().getEnchantScroll(scroll);
            final EnchantSupportItem supportTemplate = EnchantItemData.getInstance().getSupportItem(support);

            if ((scrollTemplate == null) || (supportTemplate == null) || !scrollTemplate.isValid(item, supportTemplate)) {
                // message may be custom
                activeChar.send(SystemMessageId.INAPPROPRIATE_ENCHANT_CONDITION);
                activeChar.setActiveEnchantSupportItemId(L2PcInstance.ID_NONE);
                activeChar.send(new ExPutEnchantSupportItemResult(0));
                return;
            }
            activeChar.setActiveEnchantSupportItemId(support.getObjectId());
            activeChar.send(new ExPutEnchantSupportItemResult(_supportObjectId));
        }
    }
    */
    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new RuntimeException("Not implemented yet");
    }
}
