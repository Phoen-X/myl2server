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
 * @author l3x
 */
public class RequestSetSeed extends L2GameClientPacket {
    private static final int BATCH_LENGTH = 20; // length of the one item

    private int _manorId;

    public RequestSetSeed(ByteBuffer buf) {
        super(buf);
    }
/*
    private List<SeedProduction> _items;
*/

    @Override
    protected void readImpl() {
        _manorId = readD();
/*
        final int count = readD();
        if ((count <= 0) || (count > Config.MAX_ITEM_IN_PACKET) || ((count * BATCH_LENGTH) != _buf.remaining())) {
            return;
        }

        _items = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final int itemId = readD();
            final long sales = readQ();
            final long price = readQ();
            if ((itemId < 1) || (sales < 0) || (price < 0)) {
                _items.clear();
                return;
            }

            if (sales > 0) {
                _items.add(new SeedProduction(itemId, sales, price, sales));
            }
        }
*/
    }

/*
    @Override
    protected void runImpl() {
        if (_items.isEmpty()) {
            return;
        }

        final CastleManorManager manor = CastleManorManager.getInstance();
        if (!manor.isModifiablePeriod()) {
            sendActionFailed();
            return;
        }

        // Check player privileges
        final L2PcInstance player = getActiveChar();
        if ((player == null) || (player.getClan() == null) || (player.getClan().getCastleId() != _manorId) || !player.hasClanPrivilege(ClanPrivilege.CS_MANOR_ADMIN) || !player.getLastFolkNPC().canInteract(player)) {
            sendActionFailed();
            return;
        }

        // Filter seeds with start amount lower than 0 and incorrect price
        final List<SeedProduction> list = new ArrayList<>(_items.size());
        for (SeedProduction sp : _items) {
            final L2Seed s = manor.getSeed(sp.getId());
            if ((s != null) && (sp.getStartAmount() <= s.getSeedLimit()) && (sp.getPrice() >= s.getSeedMinPrice()) && (sp.getPrice() <= s.getSeedMaxPrice())) {
                list.add(sp);
            }
        }

        // Save new list
        manor.setNextSeedProduction(list, _manorId);
    }
*/

/*
    @Override
    public String getType() {
        return "[C] D0:03 RequestSetSeed";
    }
*/

    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new RuntimeException("Not implemented yet");
    }
}