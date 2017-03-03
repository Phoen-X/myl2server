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
package com.l2server.network.clientpackets.game;

import com.l2server.network.GameServerPacketProcessor;
import com.l2server.network.L2GameClient;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author Migi, DS
 */
public final class RequestCancelPostAttachment extends L2GameClientPacket {
    private static final String _C__D0_6F_REQUESTCANCELPOSTATTACHMENT = "[C] D0:6F RequestCancelPostAttachment";

    private int _msgId;

    @Override
    protected void readImpl() {
        _msgId = readD();
    }

    /*@Override
    public void runImpl() {
        final L2PcInstance activeChar = getClient().getActiveChar();
        if ((activeChar == null) || !Config.ALLOW_MAIL || !Config.ALLOW_ATTACHMENTS) {
            return;
        }

        if (!getClient().getFloodProtectors().getTransaction().tryPerformAction("cancelpost")) {
            return;
        }

        Message msg = MailManager.getInstance().getMessage(_msgId);
        if (msg == null) {
            return;
        }
        if (msg.getSenderId() != activeChar.getObjectId()) {
            Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " tried to cancel not own post!", Config.DEFAULT_PUNISH);
            return;
        }

        if (!activeChar.isInsideZone(ZoneId.PEACE)) {
            activeChar.send(SystemMessageId.CANT_CANCEL_NOT_IN_PEACE_ZONE);
            return;
        }

        if (activeChar.getActiveTradeList() != null) {
            activeChar.send(SystemMessageId.CANT_CANCEL_DURING_EXCHANGE);
            return;
        }

        if (activeChar.isEnchanting()) {
            activeChar.send(SystemMessageId.CANT_CANCEL_DURING_ENCHANT);
            return;
        }

        if (activeChar.getPrivateStoreType() != PrivateStoreType.NONE) {
            activeChar.send(SystemMessageId.CANT_CANCEL_PRIVATE_STORE);
            return;
        }

        if (!msg.hasAttachments()) {
            activeChar.send(SystemMessageId.YOU_CANT_CANCEL_RECEIVED_MAIL);
            return;
        }

        final ItemContainer attachments = msg.getAttachments();
        if ((attachments == null) || (attachments.getSize() == 0)) {
            activeChar.send(SystemMessageId.YOU_CANT_CANCEL_RECEIVED_MAIL);
            return;
        }

        int weight = 0;
        int slots = 0;

        for (L2ItemInstance item : attachments.getItems()) {
            if (item == null) {
                continue;
            }

            if (item.getOwnerId() != activeChar.getObjectId()) {
                Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " tried to get not own item from cancelled attachment!", Config.DEFAULT_PUNISH);
                return;
            }

            if (item.getItemLocation() != ItemLocation.MAIL) {
                Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " tried to get items not from mail !", Config.DEFAULT_PUNISH);
                return;
            }

            if (item.getLocationSlot() != msg.getId()) {
                Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " tried to get items from different attachment!", Config.DEFAULT_PUNISH);
                return;
            }

            weight += item.getCount() * item.getItem().getWeight();
            if (!item.isStackable()) {
                slots += item.getCount();
            } else if (activeChar.getInventory().getItemByItemId(item.getId()) == null) {
                slots++;
            }
        }

        if (!activeChar.getInventory().validateCapacity(slots)) {
            activeChar.send(SystemMessageId.CANT_CANCEL_INVENTORY_FULL);
            return;
        }

        if (!activeChar.getInventory().validateWeight(weight)) {
            activeChar.send(SystemMessageId.CANT_CANCEL_INVENTORY_FULL);
            return;
        }

        // Proceed to the transfer
        InventoryUpdate playerIU = Config.FORCE_INVENTORY_UPDATE ? null : new InventoryUpdate();
        for (L2ItemInstance item : attachments.getItems()) {
            if (item == null) {
                continue;
            }

            long count = item.getCount();
            final L2ItemInstance newItem = attachments.transferItem(attachments.getName(), item.getObjectId(), count, activeChar.getInventory(), activeChar, null);
            if (newItem == null) {
                return;
            }

            if (playerIU != null) {
                if (newItem.getCount() > count) {
                    playerIU.addModifiedItem(newItem);
                } else {
                    playerIU.addNewItem(newItem);
                }
            }
            SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.YOU_ACQUIRED_S2_S1);
            sm.addItemName(item.getId());
            sm.addLong(count);
            activeChar.send(sm);
        }

        msg.removeAttachments();

        // Send updated item list to the player
        if (playerIU != null) {
            activeChar.send(playerIU);
        } else {
            activeChar.send(new ItemList(activeChar, false));
        }

        // Update current load status on player
        StatusUpdate su = new StatusUpdate(activeChar);
        su.addAttribute(StatusUpdate.CUR_LOAD, activeChar.getCurrentLoad());
        activeChar.send(su);

        final L2PcInstance receiver = L2World.getInstance().getPlayer(msg.getReceiverId());
        if (receiver != null) {
            SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_CANCELLED_MAIL);
            sm.addCharName(activeChar);
            receiver.send(sm);
            receiver.send(new ExChangePostState(true, _msgId, Message.DELETED));
        }

        MailManager.getInstance().deleteMessageInDb(_msgId);

        activeChar.send(new ExChangePostState(false, _msgId, Message.DELETED));
        activeChar.send(SystemMessageId.MAIL_SUCCESSFULLY_CANCELLED);
    }
    */
    @Override
    protected boolean triggersOnActionRequest() {
        return false;
    }

    @Override
    public void process(GameServerPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}