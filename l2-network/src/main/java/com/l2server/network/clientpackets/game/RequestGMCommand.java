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
 * This class ...
 *
 * @version $Revision: 1.1.2.2.2.2 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestGMCommand extends L2GameClientPacket {
    private static final String _C__7E_REQUESTGMCOMMAND = "[C] 7E RequestGMCommand";

    private String _targetName;
    private int _command;

    @Override
    protected void readImpl() {
        _targetName = readS();
        _command = readD();
        // _unknown = readD();
    }

    /*@Override
    protected void runImpl() {
        // prevent non gm or low level GMs from vieweing player stuff
        if (!getClient().getActiveChar().isGM() || !getClient().getActiveChar().getAccessLevel().allowAltG()) {
            return;
        }

        L2PcInstance player = L2World.getInstance().getPlayer(_targetName);

        L2Clan clan = ClanTable.getInstance().getClanByName(_targetName);

        // player name was incorrect?
        if ((player == null) && ((clan == null) || (_command != 6))) {
            return;
        }

        switch (_command) {
            case 1: // player status
            {
                send(new GMViewCharacterInfo(player));
                send(new GMHennaInfo(player));
                break;
            }
            case 2: // player clan
            {
                if ((player != null) && (player.getClan() != null)) {
                    send(new GMViewPledgeInfo(player.getClan(), player));
                }
                break;
            }
            case 3: // player skills
            {
                send(new GMViewSkillInfo(player));
                break;
            }
            case 4: // player quests
            {
                send(new GmViewQuestInfo(player));
                break;
            }
            case 5: // player inventory
            {
                send(new GMViewItemList(player));
                send(new GMHennaInfo(player));
                break;
            }
            case 6: // player warehouse
            {
                // gm warehouse view to be implemented
                if (player != null) {
                    send(new GMViewWarehouseWithdrawList(player));
                    // clan warehouse
                } else {
                    send(new GMViewWarehouseWithdrawList(clan));
                }
                break;
            }

        }
    }
    */

    @Override
    public void process(GameServerPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}
