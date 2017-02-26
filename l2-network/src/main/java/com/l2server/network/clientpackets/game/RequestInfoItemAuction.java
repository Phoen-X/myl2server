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
public final class RequestInfoItemAuction extends L2GameClientPacket {
    private static final String _C__D0_3A_REQUESTINFOITEMAUCTION = "[C] D0:3A RequestInfoItemAuction";

    private int _instanceId;

    @Override
    protected final void readImpl() {
        _instanceId = super.readD();
    }

    /*@Override
    protected final void runImpl() {
        final L2PcInstance activeChar = super.getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }

        if (!getClient().getFloodProtectors().getItemAuction().tryPerformAction("RequestInfoItemAuction")) {
            return;
        }

        final ItemAuctionInstance instance = ItemAuctionManager.getInstance().getManagerInstance(_instanceId);
        if (instance == null) {
            return;
        }

        final ItemAuction auction = instance.getCurrentAuction();
        if (auction == null) {
            return;
        }

        activeChar.updateLastItemAuctionRequest();
        activeChar.sendPacket(new ExItemAuctionInfoPacket(true, auction, instance.getNextAuction()));
    }
    */
    @Override
    public void process(GameServerPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}