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
 * @author Forsaiken
 */
public final class RequestBidItemAuction extends L2GameClientPacket {
    private static final String _C__D0_39_REQUESTBIDITEMAUCTION = "[C] D0:39 RequestBidItemAuction";

    private int _instanceId;
    private long _bid;

    @Override
    protected final void readImpl() {
        _instanceId = super.readD();
        _bid = super.readQ();
    }

    /*@Override
    protected final void runImpl() {
        final L2PcInstance activeChar = super.getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }

        // can't use auction fp here
        if (!getClient().getFloodProtectors().getTransaction().tryPerformAction("auction")) {
            activeChar.sendMessage("You are bidding too fast.");
            return;
        }

        if ((_bid < 0) || (_bid > Inventory.MAX_ADENA)) {
            return;
        }

        final ItemAuctionInstance instance = ItemAuctionManager.getInstance().getManagerInstance(_instanceId);
        if (instance != null) {
            final ItemAuction auction = instance.getCurrentAuction();
            if (auction != null) {
                auction.registerBid(activeChar, _bid);
            }
        }
    }
    */
    @Override
    public void process(GameServerPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}