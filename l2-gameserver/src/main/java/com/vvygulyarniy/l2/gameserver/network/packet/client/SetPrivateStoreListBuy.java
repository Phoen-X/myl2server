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
import lombok.ToString;

import java.nio.ByteBuffer;

/**
 * This class ...
 *
 * @version $Revision: 1.2.2.1.2.5 $ $Date: 2005/03/27 15:29:30 $ CPU Disasm Packets: ddhhQQ cddb
 */
@ToString
public final class SetPrivateStoreListBuy extends L2GameClientPacket {
    private static final String _C__9A_SETPRIVATESTORELISTBUY = "[C] 9A SetPrivateStoreListBuy";

    private static final int BATCH_LENGTH = 40; // length of the one item

    public SetPrivateStoreListBuy(ByteBuffer buf) {
        super(buf);
    }

    //private Item[] _items = null;

    @Override
    protected void readImpl() {
        int count = readD();
        /*if ((count < 1) || (count > Config.MAX_ITEM_IN_PACKET) || ((count * BATCH_LENGTH) != _buf.remaining())) {
            return;
        }

        _items = new Item[count];
        for (int i = 0; i < count; i++) {
            int itemId = readD();

            readD(); // TODO analyse this

            long cnt = readQ();
            long price = readQ();

            if ((itemId < 1) || (cnt < 1) || (price < 0)) {
                _items = null;
                return;
            }
            readD(); // Unk
            readD(); // Unk
            readD(); // Unk
            readD(); // Unk

            _items[i] = new Item(itemId, cnt, price);
        }*/
    }

    /*@Override
    protected void runImpl() {
        L2PcInstance player = getClient().getActiveChar();
        if (player == null) {
            return;
        }

        if (_items == null) {
            player.setPrivateStoreType(PrivateStoreType.NONE);
            player.broadcastUserInfo();
            return;
        }

        if (!player.getAccessLevel().allowTransaction()) {
            player.send(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
            return;
        }

        if (AttackStanceTaskManager.getInstance().hasAttackStanceTask(player) || player.isInDuel()) {
            player.send(SystemMessageId.CANT_OPERATE_PRIVATE_STORE_DURING_COMBAT);
            player.send(new PrivateStoreManageListBuy(player));
            player.send(ActionFailed.STATIC_PACKET);
            return;
        }

        if (player.isInsideZone(ZoneId.NO_STORE)) {
            player.send(new PrivateStoreManageListBuy(player));
            player.send(SystemMessageId.NO_PRIVATE_STORE_HERE);
            player.send(ActionFailed.STATIC_PACKET);
            return;
        }

        TradeList tradeList = player.getBuyList();
        tradeList.clear();

        // Check maximum number of allowed slots for pvt shops
        if (_items.length > player.getPrivateBuyStoreLimit()) {
            player.send(new PrivateStoreManageListBuy(player));
            player.send(SystemMessageId.YOU_HAVE_EXCEEDED_QUANTITY_THAT_CAN_BE_INPUTTED);
            return;
        }

        long totalCost = 0;
        for (Item i : _items) {
            if (!i.addToTradeList(tradeList)) {
                Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " tried to set price more than " + MAX_ADENA + " adena in Private Store - Buy.", Config.DEFAULT_PUNISH);
                return;
            }

            totalCost += i.getCost();
            if (totalCost > MAX_ADENA) {
                Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " tried to set total price more than " + MAX_ADENA + " adena in Private Store - Buy.", Config.DEFAULT_PUNISH);
                return;
            }
        }

        // Check for available funds
        if (totalCost > player.getAdena()) {
            player.send(new PrivateStoreManageListBuy(player));
            player.send(SystemMessageId.THE_PURCHASE_PRICE_IS_HIGHER_THAN_MONEY);
            return;
        }

        player.sitDown();
        player.setPrivateStoreType(PrivateStoreType.BUY);
        player.broadcastUserInfo();
        player.broadcastPacket(new PrivateStoreMsgBuy(player));
    }
    */
    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new RuntimeException("Not implemented yet");
    }

    /*private static class Item {
        private final int _itemId;
        private final long _count;
        private final long _price;

        public Item(int id, long num, long pri) {
            _itemId = id;
            _count = num;
            _price = pri;
        }

        public boolean addToTradeList(TradeList list) {
            if ((MAX_ADENA / _count) < _price) {
                return false;
            }

            list.addItemByItemId(_itemId, _count, _price);
            return true;
        }

        public long getCost() {
            return _count * _price;
        }
    }*/
}
