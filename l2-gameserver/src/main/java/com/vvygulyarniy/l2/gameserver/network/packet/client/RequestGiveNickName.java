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

public class RequestGiveNickName extends L2GameClientPacket {
    private static final String _C__0B_REQUESTGIVENICKNAME = "[C] 0B RequestGiveNickName";

    private String _target;
    private String _title;

    @Override
    protected void readImpl() {
        _target = readS();
        _title = readS();
    }

    /*@Override
    protected void runImpl() {
        L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }

        // Noblesse can bestow a title to themselves
        if (activeChar.isNoble() && _target.equalsIgnoreCase(activeChar.getName())) {
            activeChar.setTitle(_title);
            activeChar.send(SystemMessageId.TITLE_CHANGED);
            activeChar.broadcastTitleInfo();
        } else {
            // Can the player change/give a title?
            if (!activeChar.hasClanPrivilege(ClanPrivilege.CL_GIVE_TITLE)) {
                activeChar.send(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
                return;
            }

            if (activeChar.getClan().getLevel() < 3) {
                activeChar.send(SystemMessageId.CLAN_LVL_3_NEEDED_TO_ENDOWE_TITLE);
                return;
            }

            L2ClanMember member1 = activeChar.getClan().getClanMember(_target);
            if (member1 != null) {
                L2PcInstance member = member1.getPlayerInstance();
                if (member != null) {
                    // is target from the same clan?
                    member.setTitle(_title);
                    member.send(SystemMessageId.TITLE_CHANGED);
                    member.broadcastTitleInfo();
                } else {
                    activeChar.send(SystemMessageId.TARGET_IS_NOT_FOUND_IN_THE_GAME);
                }
            } else {
                activeChar.send(SystemMessageId.TARGET_MUST_BE_IN_CLAN);
            }
        }
    }
    */
    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}
