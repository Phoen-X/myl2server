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
 * @author Migi, DS
 */
public final class RequestPostAttachment extends L2GameClientPacket {
    private static final String _C__D0_6A_REQUESTPOSTATTACHMENT = "[C] D0:6A RequestPostAttachment";

    private int _msgId;

    public RequestPostAttachment(@NotNull ByteBuffer buffer) {
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

        if (!getClient().getFloodProtectors().getTransaction().tryPerformAction("getattach")) {
            return;
        }

        if (!activeChar.getAccessLevel().allowTransaction()) {
            activeChar.sendMessage("Transactions are disabled for your Access Level");
            return;
        }

        if (!activeChar.isInsideZone(ZoneId.PEACE)) {
            activeChar.send(SystemMessageId.CANT_RECEIVE_NOT_IN_PEACE_ZONE);
            return;
        }

        if (activeChar.getActiveTradeList() != null) {
            activeChar.send(SystemMessageId.CANT_RECEIVE_DURING_EXCHANGE);
            return;
        }

        if (activeChar.isEnchanting()) {
            activeChar.send(SystemMessageId.CANT_RECEIVE_DURING_ENCHANT);
            return;
        }

        if (activeChar.getPrivateStoreType() != PrivateStoreType.NONE) {
            activeChar.send(SystemMessageId.CANT_RECEIVE_PRIVATE_STORE);
            return;
        }

        final Message msg = MailManager.getInstance().getMessage(_msgId);
        if (msg == null) {
            return;
        }

        if (msg.getReceiverId() != activeChar.getObjectId()) {
            Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " tried to get not own attachment!", Config.DEFAULT_PUNISH);
            return;
        }

        if (!msg.hasAttachments()) {
            return;
        }

        final ItemContainer attachments = msg.getAttachments();
        if (attachments == null) {
            return;
        }

        int weight = 0;
        int slots = 0;

        for (L2ItemInstance item : attachments.getItems()) {
            if (item == null) {
                continue;
            }

            // Calculate needed slots
            if (item.getOwnerId() != msg.getSenderId()) {
                Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " tried to get wrong item (ownerId != senderId) from attachment!", Config.DEFAULT_PUNISH);
                return;
            }

            if (item.getItemLocation() != ItemLocation.MAIL) {
                Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " tried to get wrong item (Location != MAIL) from attachment!", Config.DEFAULT_PUNISH);
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

        // Item Max Limit Check
        if (!activeChar.getInventory().validateCapacity(slots)) {
            activeChar.send(SystemMessageId.CANT_RECEIVE_INVENTORY_FULL);
            return;
        }

        // Weight limit Check
        if (!activeChar.getInventory().validateWeight(weight)) {
            activeChar.send(SystemMessageId.CANT_RECEIVE_INVENTORY_FULL);
            return;
        }

        long adena = msg.getReqAdena();
        if ((adena > 0) && !activeChar.reduceAdena("PayMail", adena, null, true)) {
            activeChar.send(SystemMessageId.CANT_RECEIVE_NO_ADENA);
            return;
        }

        // Proceed to the transfer
        InventoryUpdate playerIU = Config.FORCE_INVENTORY_UPDATE ? null : new InventoryUpdate();
        for (L2ItemInstance item : attachments.getItems()) {
            if (item == null) {
                continue;
            }

            if (item.getOwnerId() != msg.getSenderId()) {
                Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " tried to get items with owner != sender !", Config.DEFAULT_PUNISH);
                return;
            }

            long count = item.getCount();
            final L2ItemInstance newItem = attachments.transferItem(attachments.getName(), item.getObjectId(), item.getCount(), activeChar.getInventory(), activeChar, null);
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

        // Send updated item list to the player
        if (playerIU != null) {
            activeChar.send(playerIU);
        } else {
            activeChar.send(new ItemList(activeChar, false));
        }

        msg.removeAttachments();

        // Update current load status on player
        StatusUpdate su = new StatusUpdate(activeChar);
        su.addAttribute(StatusUpdate.CUR_LOAD, activeChar.getCurrentLoad());
        activeChar.send(su);

        SystemMessage sm;
        final L2PcInstance sender = L2World.getInstance().getPlayer(msg.getSenderId());
        if (adena > 0) {
            if (sender != null) {
                sender.addAdena("PayMail", adena, activeChar, false);
                sm = SystemMessage.getSystemMessage(SystemMessageId.PAYMENT_OF_S1_ADENA_COMPLETED_BY_S2);
                sm.addLong(adena);
                sm.addCharName(activeChar);
                sender.send(sm);
            } else {
                L2ItemInstance paidAdena = ItemTable.getInstance().createItem("PayMail", ADENA_ID, adena, activeChar, null);
                paidAdena.setOwnerId(msg.getSenderId());
                paidAdena.setItemLocation(ItemLocation.INVENTORY);
                paidAdena.updateDatabase(true);
                L2World.getInstance().removeObject(paidAdena);
            }
        } else if (sender != null) {
            sm = SystemMessage.getSystemMessage(SystemMessageId.S1_ACQUIRED_ATTACHED_ITEM);
            sm.addCharName(activeChar);
            sender.send(sm);
        }

        activeChar.send(new ExChangePostState(true, _msgId, Message.READED));
        activeChar.send(SystemMessageId.MAIL_SUCCESSFULLY_RECEIVED);
    }
    */
    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new RuntimeException("Not implemented yet");
    }
}
