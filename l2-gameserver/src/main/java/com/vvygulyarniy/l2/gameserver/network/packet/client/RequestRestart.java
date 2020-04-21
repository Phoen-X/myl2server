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
import java.util.logging.Logger;

/**
 * This class ...
 *
 * @version $Revision: 1.11.2.1.2.4 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestRestart extends L2GameClientPacket {
    protected static final Logger _logAccounting = Logger.getLogger("accounting");
    private static final String _C__57_REQUESTRESTART = "[C] 57 RequestRestart";

    public RequestRestart(ByteBuffer buf) {
        super(buf);
    }

    @Override
    protected void readImpl() {
        // trigger
    }

    /*@Override
    protected void runImpl() {
        final L2PcInstance player = getClient().getActiveChar();

        if (player == null) {
            return;
        }

        if ((player.getActiveEnchantItemId() != L2PcInstance.ID_NONE) || (player.getActiveEnchantAttrItemId() != L2PcInstance.ID_NONE)) {
            send(RestartResponse.valueOf(false));
            return;
        }

        if (player.isLocked()) {
            _log.warning("Player " + player.getName() + " tried to restart during class change.");
            send(RestartResponse.valueOf(false));
            return;
        }

        if (player.getPrivateStoreType() != PrivateStoreType.NONE) {
            player.sendMessage("Cannot restart while trading");
            send(RestartResponse.valueOf(false));
            return;
        }

        if (AttackStanceTaskManager.getInstance().hasAttackStanceTask(player) && !(player.isGM() && Config.GM_RESTART_FIGHTING)) {
            if (Config.DEBUG) {
                _log.fine("Player " + player.getName() + " tried to logout while fighting.");
            }

            player.send(SystemMessageId.CANT_RESTART_WHILE_FIGHTING);
            send(RestartResponse.valueOf(false));
            return;
        }

        // Prevent player from restarting if they are a festival participant
        // and it is in progress, otherwise notify party members that the player
        // is not longer a participant.
        if (player.isFestivalParticipant()) {
            if (SevenSignsFestival.getInstance().isFestivalInitialized()) {
                player.sendMessage("You cannot restart while you are a participant in a festival.");
                send(RestartResponse.valueOf(false));
                return;
            }

            final L2Party playerParty = player.getParty();

            if (playerParty != null) {
                player.getParty().broadcastString(player.getName() + " has been removed from the upcoming festival.");
            }
        }

        if (player.isBlockedFromExit()) {
            send(RestartResponse.valueOf(false));
            return;
        }

        // Remove player from Boss Zone
        player.removeFromBossZone();

        final L2GameClient client = getClient();

        LogRecord record = new LogRecord(Level.INFO, "Logged out");
        record.setParameters(new Object[]
                {
                        client
                });
        _logAccounting.log(record);

        // detach the client from the char so that the connection isnt closed in the deleteMe
        player.setClient(null);

        player.deleteMe();

        client.setActiveChar(null);
        AntiFeedManager.getInstance().onDisconnect(client);

        // return the client to the authed status
        client.setState(GameClientState.IN_LOBBY);

        send(RestartResponse.valueOf(true));

        // send char list
        final CharSelectionInfo cl = new CharSelectionInfo(client.getAccountName(), client.getSessionId().playOkID1);
        send(cl);
        client.setCharSelection(cl.getCharInfo());
    }
    */
    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new RuntimeException("Not implemented yet");
    }
}