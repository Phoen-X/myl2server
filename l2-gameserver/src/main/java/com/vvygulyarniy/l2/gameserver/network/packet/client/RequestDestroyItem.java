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

/**
 * This class ...
 *
 * @version $Revision: 1.7.2.4.2.6 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestDestroyItem extends L2GameClientPacket {
    private static final String _C__60_REQUESTDESTROYITEM = "[C] 60 RequestDestroyItem";

    private int _objectId;
    private long _count;

    public RequestDestroyItem(ByteBuffer buf) {
        super(buf);
    }

    @Override
    protected void readImpl() {
        _objectId = readD();
        _count = readQ();
    }

   /* @Override
    protected void runImpl() {
        L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }

        if (_count <= 0) {
            if (_count < 0) {
                Util.handleIllegalPlayerAction(activeChar, "[RequestDestroyItem] Character " + activeChar.getName() + " of account " + activeChar.getAccountName() + " tried to destroy item with oid " + _objectId + " but has count < 0!", Config.DEFAULT_PUNISH);
            }
            return;
        }

        if (!getClient().getFloodProtectors().getTransaction().tryPerformAction("destroy")) {
            activeChar.sendMessage("You are destroying items too fast.");
            return;
        }

        long count = _count;

        if (activeChar.isProcessingTransaction() || (activeChar.getPrivateStoreType() != PrivateStoreType.NONE)) {
            activeChar.send(SystemMessageId.CANNOT_TRADE_DISCARD_DROP_ITEM_WHILE_IN_SHOPMODE);
            return;
        }

        L2ItemInstance itemToRemove = activeChar.getInventory().getItemByObjectId(_objectId);

        // if we can't find the requested item, its actually a cheat
        if (itemToRemove == null) {
            activeChar.send(SystemMessageId.CANNOT_DISCARD_THIS_ITEM);
            return;
        }

        // Cannot discard item that the skill is consuming
        if (activeChar.isCastingNow()) {
            if ((activeChar.getCurrentSkill() != null) && (activeChar.getCurrentSkill().getSkill().getItemConsumeId() == itemToRemove.getId())) {
                activeChar.send(SystemMessageId.CANNOT_DISCARD_THIS_ITEM);
                return;
            }
        }
        // Cannot discard item that the skill is consuming
        if (activeChar.isCastingSimultaneouslyNow()) {
            if ((activeChar.getLastSimultaneousSkillCast() != null) && (activeChar.getLastSimultaneousSkillCast().getItemConsumeId() == itemToRemove.getId())) {
                activeChar.send(SystemMessageId.CANNOT_DISCARD_THIS_ITEM);
                return;
            }
        }

        int itemId = itemToRemove.getId();

        if ((!activeChar.canOverrideCond(PcCondOverride.DESTROY_ALL_ITEMS) && !itemToRemove.isDestroyable()) || CursedWeaponsManager.getInstance().isCursed(itemId)) {
            if (itemToRemove.isHeroItem()) {
                activeChar.send(SystemMessageId.HERO_WEAPONS_CANT_DESTROYED);
            } else {
                activeChar.send(SystemMessageId.CANNOT_DISCARD_THIS_ITEM);
            }
            return;
        }

        if (!itemToRemove.isStackable() && (count > 1)) {
            Util.handleIllegalPlayerAction(activeChar, "[RequestDestroyItem] Character " + activeChar.getName() + " of account " + activeChar.getAccountName() + " tried to destroy a non-stackable item with oid " + _objectId + " but has count > 1!", Config.DEFAULT_PUNISH);
            return;
        }

        if (!activeChar.getInventory().canManipulateWithItemId(itemToRemove.getId())) {
            activeChar.sendMessage("You cannot use this item.");
            return;
        }

        if (_count > itemToRemove.getCount()) {
            count = itemToRemove.getCount();
        }

        if (itemToRemove.getItem().isPetItem()) {
            if (activeChar.hasSummon() && (activeChar.getSummon().getControlObjectId() == _objectId)) {
                activeChar.getSummon().unSummon(activeChar);
            }

            try (Connection con = ConnectionFactory.getInstance().getConnection();
                 PreparedStatement statement = con.prepareStatement("DELETE FROM pets WHERE item_obj_id=?")) {
                statement.setInt(1, _objectId);
                statement.execute();
            } catch (Exception e) {
                _log.log(Level.WARNING, "could not delete pet objectid: ", e);
            }
        }
        if (itemToRemove.isTimeLimitedItem()) {
            itemToRemove.endOfLife();
        }

        if (itemToRemove.isEquipped()) {
            if (itemToRemove.getEnchantLevel() > 0) {
                SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.EQUIPMENT_S1_S2_REMOVED);
                sm.addInt(itemToRemove.getEnchantLevel());
                sm.addItemName(itemToRemove);
                activeChar.send(sm);
            } else {
                SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_DISARMED);
                sm.addItemName(itemToRemove);
                activeChar.send(sm);
            }

            L2ItemInstance[] unequiped = activeChar.getInventory().unEquipItemInSlotAndRecord(itemToRemove.getLocationSlot());

            InventoryUpdate iu = new InventoryUpdate();
            for (L2ItemInstance itm : unequiped) {
                iu.addModifiedItem(itm);
            }
            activeChar.send(iu);
        }

        L2ItemInstance removedItem = activeChar.getInventory().destroyItem("Destroy", itemToRemove, count, activeChar, null);

        if (removedItem == null) {
            return;
        }

        if (!Config.FORCE_INVENTORY_UPDATE) {
            InventoryUpdate iu = new InventoryUpdate();
            if (removedItem.getCount() == 0) {
                iu.addRemovedItem(removedItem);
            } else {
                iu.addModifiedItem(removedItem);
            }
            activeChar.send(iu);
        } else {
            send(new ItemList(activeChar, true));
        }

        StatusUpdate su = new StatusUpdate(activeChar);
        su.addAttribute(StatusUpdate.CUR_LOAD, activeChar.getCurrentLoad());
        activeChar.send(su);
    }*/

    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}
