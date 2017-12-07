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
 * @author l3x
 */
public class RequestProcureCropList extends L2GameClientPacket {
    private static final int BATCH_LENGTH = 20; // length of the one item

    public RequestProcureCropList(ByteBuffer buf) {
        super(buf);
    }

    //private List<CropHolder> _items = null; //TODO uncomment this

    //TODO uncomment this
    @Override
    protected final void readImpl() {
        final int count = readD();
        /*
        if ((count <= 0) || (count > Config.MAX_ITEM_IN_PACKET) || ((count * BATCH_LENGTH) != _buf.remaining())) {
            return;
        }

        _items = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final int objId = readD();
            final int itemId = readD();
            final int manorId = readD();
            final long cnt = readQ();
            if ((objId < 1) || (itemId < 1) || (manorId < 0) || (cnt < 0)) {
                _items = null;
                return;
            }
            _items.add(new CropHolder(objId, itemId, cnt, manorId));
        }*/
    }

    /*@Override
    protected final void runImpl() {
        if (_items == null) {
            return;
        }

        final L2PcInstance player = getActiveChar();
        if (player == null) {
            return;
        }

        final CastleManorManager manor = CastleManorManager.getInstance();
        if (manor.isUnderMaintenance()) {
            sendActionFailed();
            return;
        }

        final L2Npc manager = player.getLastFolkNPC();
        if (!(manager instanceof L2MerchantInstance) || !manager.canInteract(player)) {
            sendActionFailed();
            return;
        }

        final int castleId = manager.getCastle().getResidenceId();
        if (manager.getTemplate().getParameters().getInt("manor_id", -1) != castleId) {
            sendActionFailed();
            return;
        }

        int slots = 0, weight = 0;
        for (CropHolder i : _items) {
            final L2ItemInstance item = player.getInventory().getItemByObjectId(i.getObjectId());
            if ((item == null) || (item.getCount() < i.getCount()) || (item.getId() != i.getId())) {
                sendActionFailed();
                return;
            }

            final CropProcure cp = i.getCropProcure();
            if ((cp == null) || (cp.getAmount() < i.getCount())) {
                sendActionFailed();
                return;
            }

            final L2Item template = ItemTable.getInstance().getTemplate(i.getRewardId());
            weight += (i.getCount() * template.getWeight());

            if (!template.isStackable()) {
                slots += i.getCount();
            } else if (player.getInventory().getItemByItemId(i.getRewardId()) == null) {
                slots++;
            }
        }

        if (!player.getInventory().validateWeight(weight)) {
            player.send(SystemMessageId.WEIGHT_LIMIT_EXCEEDED);
            return;
        } else if (!player.getInventory().validateCapacity(slots)) {
            player.send(SystemMessageId.SLOTS_FULL);
            return;
        }

        // Used when Config.ALT_MANOR_SAVE_ALL_ACTIONS == true
        final int updateListSize = Config.ALT_MANOR_SAVE_ALL_ACTIONS ? _items.size() : 0;
        final List<CropProcure> updateList = new ArrayList<>(updateListSize);

        // Proceed the purchase
        for (CropHolder i : _items) {
            final long rewardPrice = ItemTable.getInstance().getTemplate(i.getRewardId()).getReferencePrice();
            if (rewardPrice == 0) {
                continue;
            }

            final long rewardItemCount = i.getPrice() / rewardPrice;
            if (rewardItemCount < 1) {
                final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.FAILED_IN_TRADING_S2_OF_CROP_S1);
                sm.addItemName(i.getId());
                sm.addLong(i.getCount());
                player.send(sm);
                continue;
            }

            // Fee for selling to other manors
            final long fee = (castleId == i.getManorId()) ? 0 : ((long) (i.getPrice() * 0.05));
            if ((fee != 0) && (player.getAdena() < fee)) {
                SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.FAILED_IN_TRADING_S2_OF_CROP_S1);
                sm.addItemName(i.getId());
                sm.addLong(i.getCount());
                player.send(sm);

                sm = SystemMessage.getSystemMessage(SystemMessageId.YOU_NOT_ENOUGH_ADENA);
                player.send(sm);
                continue;
            }

            final CropProcure cp = i.getCropProcure();
            if (!cp.decreaseAmount(i.getCount()) || ((fee > 0) && !player.reduceAdena("Manor", fee, manager, true)) || !player.destroyItem("Manor", i.getObjectId(), i.getCount(), manager, true)) {
                continue;
            }
            player.addItem("Manor", i.getRewardId(), rewardItemCount, manager, true);

            if (Config.ALT_MANOR_SAVE_ALL_ACTIONS) {
                updateList.add(cp);
            }
        }

        if (Config.ALT_MANOR_SAVE_ALL_ACTIONS) {
            manor.updateCurrentProcure(castleId, updateList);
        }
    }
    */
    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }

    /*private final class CropHolder extends UniqueItemHolder {
        private final int _manorId;
        private CropProcure _cp;
        private int _rewardId = 0;

        public CropHolder(int objectId, int id, long count, int manorId) {
            super(id, objectId, count);
            _manorId = manorId;
        }

        public final int getManorId() {
            return _manorId;
        }

        public final long getPrice() {
            return getCount() * _cp.getPrice();
        }

        public final CropProcure getCropProcure() {
            if (_cp == null) {
                _cp = CastleManorManager.getInstance().getCropProcure(_manorId, getId(), false);
            }
            return _cp;
        }

        public final int getRewardId() {
            if (_rewardId == 0) {
                _rewardId = CastleManorManager.getInstance().getSeedByCrop(_cp.getId()).getReward(_cp.getReward());
            }
            return _rewardId;
        }
    }*/
}