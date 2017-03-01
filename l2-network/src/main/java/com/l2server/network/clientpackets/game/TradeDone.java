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
import lombok.ToString;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * This packet manages the trade response.
 */
@ToString
public final class TradeDone extends L2GameClientPacket {
    private int _response;

    @Override
    protected void readImpl() {
        _response = readD();
    }

/*
    @Override
    protected void runImpl() {
        final L2PcInstance player = getActiveChar();
        if (player == null) {
            return;
        }

        if (!getClient().getFloodProtectors().getTransaction().tryPerformAction("trade")) {
            player.sendMessage("You are trading too fast.");
            return;
        }

        final TradeList trade = player.getActiveTradeList();
        if (trade == null) {
            if (Config.DEBUG) {
                _log.warning("player.getTradeList == null in " + getType() + " for player " + player.getName());
            }
            return;
        }

        if (trade.isLocked()) {
            return;
        }

        if (_response == 1) {
            if ((trade.getPartner() == null) || (L2World.getInstance().getPlayer(trade.getPartner().getObjectId()) == null)) {
                // Trade partner not found, cancel trade
                player.cancelActiveTrade();
                player.sendPacket(SystemMessageId.TARGET_IS_NOT_FOUND_IN_THE_GAME);
                return;
            }

            if ((trade.getOwner().getActiveEnchantItemId() != L2PcInstance.ID_NONE) || (trade.getPartner().getActiveEnchantItemId() != L2PcInstance.ID_NONE)) {
                return;
            }

            if (!player.getAccessLevel().allowTransaction()) {
                player.cancelActiveTrade();
                player.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
                return;
            }

            if ((player.getInstanceId() != trade.getPartner().getInstanceId()) && (player.getInstanceId() != -1)) {
                player.cancelActiveTrade();
                return;
            }

            if (player.calculateDistance(trade.getPartner(), true, false) > 150) {
                player.cancelActiveTrade();
                return;
            }
            trade.confirm();
        } else {
            player.cancelActiveTrade();
        }
    }
*/


    @Override
    public void process(GameServerPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}
