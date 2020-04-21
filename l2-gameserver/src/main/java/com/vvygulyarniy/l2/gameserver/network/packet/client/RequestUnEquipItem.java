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
 * @author Zoey76
 */
public class RequestUnEquipItem extends L2GameClientPacket {
    private static final String _C__16_REQUESTUNEQUIPITEM = "[C] 16 RequestUnequipItem";

    private int _slot;

    public RequestUnEquipItem(ByteBuffer buf) {
        super(buf);
    }

    /**
     * Packet type id 0x16 format: cd
     */
    @Override
    protected void readImpl() {
        _slot = readD();
    }

/*
    @Override
    protected void runImpl() {
        if (Config.DEBUG) {
            _log.fine("Request unequip slot " + _slot);
        }

        final L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }

        final L2ItemInstance item = activeChar.getInventory().getPaperdollItemByL2ItemId(_slot);
        // Wear-items are not to be unequipped.
        if (item == null) {
            return;
        }

        // The English system message say weapon, but it's applied to any equipped item.
        if (activeChar.isAttackingNow() || activeChar.isCastingNow() || activeChar.isCastingSimultaneouslyNow()) {
            activeChar.send(SystemMessageId.CANNOT_CHANGE_WEAPON_DURING_AN_ATTACK);
            return;
        }

        // Arrows and bolts.
        if ((_slot == L2Item.SLOT_L_HAND) && (item.getItem() instanceof L2EtcItem)) {
            return;
        }

        // Prevent of unequipping a cursed weapon.
        if ((_slot == L2Item.SLOT_LR_HAND) && (activeChar.isCursedWeaponEquipped() || activeChar.isCombatFlagEquipped())) {
            return;
        }

        // Prevent player from unequipping items in special conditions.
        if (activeChar.isStunned() || activeChar.isSleeping() || activeChar.isParalyzed() || activeChar.isAlikeDead()) {
            return;
        }

        if (!activeChar.getInventory().canManipulateWithItemId(item.getId())) {
            activeChar.send(SystemMessageId.ITEM_CANNOT_BE_TAKEN_OFF);
            return;
        }

        if (item.isWeapon() && item.getWeaponItem().isForceEquip() && !activeChar.canOverrideCond(PcCondOverride.ITEM_CONDITIONS)) {
            activeChar.send(SystemMessageId.ITEM_CANNOT_BE_TAKEN_OFF);
            return;
        }

        final L2ItemInstance[] unequipped = activeChar.getInventory().unEquipItemInBodySlotAndRecord(_slot);
        activeChar.broadcastUserInfo();

        // This can be 0 if the user pressed the right mouse button twice very fast.
        if (unequipped.length > 0) {
            SystemMessage sm = null;
            if (unequipped[0].getEnchantLevel() > 0) {
                sm = SystemMessage.getSystemMessage(SystemMessageId.EQUIPMENT_S1_S2_REMOVED);
                sm.addInt(unequipped[0].getEnchantLevel());
            } else {
                sm = SystemMessage.getSystemMessage(SystemMessageId.S1_DISARMED);
            }
            sm.addItemName(unequipped[0]);
            activeChar.send(sm);

            InventoryUpdate iu = new InventoryUpdate();
            iu.addItems(Arrays.asList(unequipped));
            activeChar.send(iu);
        }
    }

*/

    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new RuntimeException("Not implemented yet");
    }
}
