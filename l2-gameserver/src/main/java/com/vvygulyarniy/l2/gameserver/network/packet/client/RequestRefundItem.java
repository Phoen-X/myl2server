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

/**
 * RequestRefundItem client packet class.
 */
public final class RequestRefundItem extends L2GameClientPacket {
    private static final String _C__D0_75_REQUESTREFUNDITEM = "[C] D0:75 RequestRefundItem";

    private static final int BATCH_LENGTH = 4; // length of the one item

    private int _listId;
    private int[] _items = null;

    @Override
    protected void readImpl() {
        _listId = readD();
        final int count = readD();
        if ((count <= 0) || (count > 20) || ((count * BATCH_LENGTH) != getBuffer().remaining())) {
            return;
        }

        _items = new int[count];
        for (int i = 0; i < count; i++) {
            _items[i] = readD();
        }
    }

    /*@Override
    protected void runImpl() {
        final L2PcInstance player = getClient().getActiveChar();
        if (player == null) {
            return;
        }

        if (!getClient().getFloodProtectors().getTransaction().tryPerformAction("refund")) {
            player.sendMessage("You are using refund too fast.");
            return;
        }

        if (_items == null) {
            send(ActionFailed.STATIC_PACKET);
            return;
        }

        if (!player.hasRefund()) {
            send(ActionFailed.STATIC_PACKET);
            return;
        }

        L2Object target = player.getTarget();
        if (!player.isGM() && ((target == null) || !(target instanceof L2MerchantInstance) || (player.getInstanceId() != target.getInstanceId()) || !player.isInsideRadius(target, INTERACTION_DISTANCE, true, false))) {
            send(ActionFailed.STATIC_PACKET);
            return;
        }

        L2Character merchant = null;
        if (target instanceof L2MerchantInstance) {
            merchant = (L2Character) target;
        } else if (!player.isGM()) {
            send(ActionFailed.STATIC_PACKET);
            return;
        }

        if (merchant == null) {
            send(ActionFailed.STATIC_PACKET);
            return;
        }

        final L2BuyList buyList = BuyListData.getInstance().getBuyList(_listId);
        if (buyList == null) {
            Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " sent a false BuyList list_id " + _listId, Config.DEFAULT_PUNISH);
            return;
        }

        if (!buyList.isNpcAllowed(merchant.getId())) {
            send(ActionFailed.STATIC_PACKET);
            return;
        }

        long weight = 0;
        long adena = 0;
        long slots = 0;

        L2ItemInstance[] refund = player.getRefund().getItems();
        int[] objectIds = new int[_items.length];

        for (int i = 0; i < _items.length; i++) {
            int idx = _items[i];
            if ((idx < 0) || (idx >= refund.length)) {
                Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " sent invalid refund index", Config.DEFAULT_PUNISH);
                return;
            }

            // check for duplicates - indexes
            for (int j = i + 1; j < _items.length; j++) {
                if (idx == _items[j]) {
                    Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " sent duplicate refund index", Config.DEFAULT_PUNISH);
                    return;
                }
            }

            final L2ItemInstance item = refund[idx];
            final L2Item template = item.getItem();
            objectIds[i] = item.getObjectId();

            // second check for duplicates - object ids
            for (int j = 0; j < i; j++) {
                if (objectIds[i] == objectIds[j]) {
                    Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " has duplicate items in refund list", Config.DEFAULT_PUNISH);
                    return;
                }
            }

            long count = item.getCount();
            weight += count * template.getWeight();
            adena += (count * template.getReferencePrice()) / 2;
            if (!template.isStackable()) {
                slots += count;
            } else if (player.getInventory().getItemByItemId(template.getId()) == null) {
                slots++;
            }
        }

        if ((weight > Integer.MAX_VALUE) || (weight < 0) || !player.getInventory().validateWeight((int) weight)) {
            player.send(SystemMessageId.WEIGHT_LIMIT_EXCEEDED);
            send(ActionFailed.STATIC_PACKET);
            return;
        }

        if ((slots > Integer.MAX_VALUE) || (slots < 0) || !player.getInventory().validateCapacity((int) slots)) {
            player.send(SystemMessageId.SLOTS_FULL);
            send(ActionFailed.STATIC_PACKET);
            return;
        }

        if ((adena < 0) || !player.reduceAdena("Refund", adena, player.getLastFolkNPC(), false)) {
            player.send(SystemMessageId.YOU_NOT_ENOUGH_ADENA);
            send(ActionFailed.STATIC_PACKET);
            return;
        }

        for (int i = 0; i < _items.length; i++) {
            L2ItemInstance item = player.getRefund().transferItem("Refund", objectIds[i], Long.MAX_VALUE, player.getInventory(), player, player.getLastFolkNPC());
            if (item == null) {
                _log.warning("Error refunding object for char " + player.getName() + " (newitem == null)");
                continue;
            }
        }

        // Update current load status on player
        StatusUpdate su = new StatusUpdate(player);
        su.addAttribute(StatusUpdate.CUR_LOAD, player.getCurrentLoad());
        player.send(su);
        player.send(new ExBuySellList(player, true));
    }
    */
    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}
