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

import java.util.ArrayList;
import java.util.List;

public final class RequestBuyItem extends L2GameClientPacket {
    private static final String _C__40_REQUESTBUYITEM = "[C] 40 RequestBuyItem";

    private static final int BATCH_LENGTH = 12;
    private int _listId;
    private List _items = null;

    @Override
    protected void readImpl() {
        //TODO vvygulyarniy: needed to remove comments. They are left in order not to create additional classes before needed
        _listId = readD();
        int size = readD();
        /*if ((size <= 0) || (size > Config.MAX_ITEM_IN_PACKET) || ((size * BATCH_LENGTH) != _buf.remaining())) {
            return;
        }*/

        _items = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            int itemId = readD();
            long count = readQ();
            if ((itemId < 1) || (count < 1)) {
                _items = null;
                return;
            }
           /* _items.add(new ItemHolder(itemId, count));*/
        }
    }

/*
    @Override
    protected void runImpl() {
        L2PcInstance player = getClient().getActiveChar();
        if (player == null) {
            return;
        }

        if (!getClient().getFloodProtectors().getTransaction().tryPerformAction("buy")) {
            player.sendMessage("You are buying too fast.");
            return;
        }

        if (_items == null) {
            send(ActionFailed.STATIC_PACKET);
            return;
        }

        // Alt game - Karma punishment
        if (!Config.ALT_GAME_KARMA_PLAYER_CAN_SHOP && (player.getKarma() > 0)) {
            send(ActionFailed.STATIC_PACKET);
            return;
        }

        L2Object target = player.getTarget();
        L2Character merchant = null;
        if (!player.isGM()) {
            if (!(target instanceof L2MerchantInstance) || (!player.isInsideRadius(target, INTERACTION_DISTANCE, true, false)) || (player.getInstanceId() != target.getInstanceId())) {
                send(ActionFailed.STATIC_PACKET);
                return;
            }
            merchant = (L2Character) target;
        }

        double castleTaxRate = 0;
        double baseTaxRate = 0;

        if ((merchant == null) && !player.isGM()) {
            send(ActionFailed.STATIC_PACKET);
            return;
        }

        final L2BuyList buyList = BuyListData.getInstance().getBuyList(_listId);
        if (buyList == null) {
            Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " sent a false BuyList list_id " + _listId, Config.DEFAULT_PUNISH);
            return;
        }

        if (merchant != null) {
            if (!buyList.isNpcAllowed(merchant.getId())) {
                send(ActionFailed.STATIC_PACKET);
                return;
            }

            if (merchant instanceof L2MerchantInstance) {
                castleTaxRate = ((L2MerchantInstance) merchant).getMpc().getCastleTaxRate();
                baseTaxRate = ((L2MerchantInstance) merchant).getMpc().getBaseTaxRate();
            } else {
                baseTaxRate = 0.5;
            }
        }

        long subTotal = 0;

        // Check for buylist validity and calculates summary values
        long slots = 0;
        long weight = 0;
        for (ItemHolder i : _items) {
            long price = -1;

            final Product product = buyList.getProductByItemId(i.getId());
            if (product == null) {
                Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " sent a false BuyList list_id " + _listId + " and item_id " + i.getId(), Config.DEFAULT_PUNISH);
                return;
            }

            if (!product.getItem().isStackable() && (i.getCount() > 1)) {
                Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " tried to purchase invalid quantity of items at the same time.", Config.DEFAULT_PUNISH);
                send(SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_EXCEEDED_QUANTITY_THAT_CAN_BE_INPUTTED));
                return;
            }

            price = product.getPrice();
            if ((product.getItemId() >= 3960) && (product.getItemId() <= 4026)) {
                price *= Config.RATE_SIEGE_GUARDS_PRICE;
            }

            if (price < 0) {
                _log.warning("ERROR, no price found .. wrong buylist ??");
                send(ActionFailed.STATIC_PACKET);
                return;
            }

            if ((price == 0) && !player.isGM() && Config.ONLY_GM_ITEMS_FREE) {
                player.sendMessage("Ohh Cheat dont work? You have a problem now!");
                Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " tried buy item for 0 adena.", Config.DEFAULT_PUNISH);
                return;
            }

            if (product.hasLimitedStock()) {
                // trying to buy more then available
                if (i.getCount() > product.getCount()) {
                    send(ActionFailed.STATIC_PACKET);
                    return;
                }
            }

            if ((MAX_ADENA / i.getCount()) < price) {
                Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " tried to purchase over " + MAX_ADENA + " adena worth of goods.", Config.DEFAULT_PUNISH);
                return;
            }
            // first calculate price per item with tax, then multiply by count
            price = (long) (price * (1 + castleTaxRate + baseTaxRate));
            subTotal += i.getCount() * price;
            if (subTotal > MAX_ADENA) {
                Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " tried to purchase over " + MAX_ADENA + " adena worth of goods.", Config.DEFAULT_PUNISH);
                return;
            }

            weight += i.getCount() * product.getItem().getWeight();
            if (player.getInventory().getItemByItemId(product.getItemId()) == null) {
                slots++;
            }
        }

        if (!player.isGM() && ((weight > Integer.MAX_VALUE) || (weight < 0) || !player.getInventory().validateWeight((int) weight))) {
            player.send(SystemMessageId.WEIGHT_LIMIT_EXCEEDED);
            send(ActionFailed.STATIC_PACKET);
            return;
        }

        if (!player.isGM() && ((slots > Integer.MAX_VALUE) || (slots < 0) || !player.getInventory().validateCapacity((int) slots))) {
            player.send(SystemMessageId.SLOTS_FULL);
            send(ActionFailed.STATIC_PACKET);
            return;
        }

        // Charge buyer and add tax to castle treasury if not owned by npc clan
        if ((subTotal < 0) || !player.reduceAdena("Buy", subTotal, player.getLastFolkNPC(), false)) {
            player.send(SystemMessageId.YOU_NOT_ENOUGH_ADENA);
            send(ActionFailed.STATIC_PACKET);
            return;
        }

        // Proceed the purchase
        for (ItemHolder i : _items) {
            Product product = buyList.getProductByItemId(i.getId());
            if (product == null) {
                Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " sent a false BuyList list_id " + _listId + " and item_id " + i.getId(), Config.DEFAULT_PUNISH);
                continue;
            }

            if (product.hasLimitedStock()) {
                if (product.decreaseCount(i.getCount())) {
                    player.getInventory().addItem("Buy", i.getId(), i.getCount(), player, merchant);
                }
            } else {
                player.getInventory().addItem("Buy", i.getId(), i.getCount(), player, merchant);
            }
        }

        // add to castle treasury
        if (merchant instanceof L2MerchantInstance) {
            ((L2MerchantInstance) merchant).getCastle().addToTreasury((long) (subTotal * castleTaxRate));
        }

        StatusUpdate su = new StatusUpdate(player);
        su.addAttribute(StatusUpdate.CUR_LOAD, player.getCurrentLoad());
        player.send(su);
        player.send(new ExBuySellList(player, true));
    }

*/

    @Override
    public void process(GameServerPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}
