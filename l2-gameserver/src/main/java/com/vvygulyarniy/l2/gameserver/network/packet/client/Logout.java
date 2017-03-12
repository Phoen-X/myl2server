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
 * This class ...
 *
 * @version $Revision: 1.9.4.3 $ $Date: 2005/03/27 15:29:30 $
 */
public final class Logout extends L2GameClientPacket {
    private static final String _C__00_LOGOUT = "[C] 00 Logout";

    @Override
    protected void readImpl() {

    }

  /*  @Override
    protected void runImpl() {
        final L2PcInstance player = getClient().getActiveChar();
        if (player == null) {
            return;
        }

        if ((player.getActiveEnchantItemId() != L2PcInstance.ID_NONE) || (player.getActiveEnchantAttrItemId() != L2PcInstance.ID_NONE)) {
            if (Config.DEBUG) {
                _log.fine("Player " + player.getName() + " tried to logout while enchanting.");
            }
            player.send(ActionFailed.STATIC_PACKET);
            return;
        }

        if (player.isLocked()) {
            _log.warning("Player " + player.getName() + " tried to logout during class change.");
            player.send(ActionFailed.STATIC_PACKET);
            return;
        }

        // Don't allow leaving if player is fighting
        if (AttackStanceTaskManager.getInstance().hasAttackStanceTask(player)) {
            if (player.isGM() && Config.GM_RESTART_FIGHTING) {
                return;
            }

            if (Config.DEBUG) {
                _log.fine("Player " + player.getName() + " tried to logout while fighting.");
            }

            player.send(SystemMessageId.CANT_LOGOUT_WHILE_FIGHTING);
            player.send(ActionFailed.STATIC_PACKET);
            return;
        }

        if (L2Event.isParticipant(player)) {
            player.sendMessage("A superior power doesn't allow you to leave the event.");
            player.send(ActionFailed.STATIC_PACKET);
            return;
        }

        // Prevent player from logging out if they are a festival participant
        // and it is in progress, otherwise notify party members that the player
        // is not longer a participant.
        if (player.isFestivalParticipant()) {
            if (SevenSignsFestival.getInstance().isFestivalInitialized()) {
                player.sendMessage("You cannot log out while you are a participant in a Festival.");
                player.send(ActionFailed.STATIC_PACKET);
                return;
            }

            if (player.isInParty()) {
                player.getParty().broadcastPacket(SystemMessage.sendString(player.getName() + " has been removed from the upcoming Festival."));
            }
        }

        // Remove player from Boss Zone
        player.removeFromBossZone();

        LogRecord record = new LogRecord(Level.INFO, "Disconnected");
        record.setParameters(new Object[]
                {
                        getClient()
                });
        _logAccounting.log(record);

        player.logout();
    }
    */

    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}