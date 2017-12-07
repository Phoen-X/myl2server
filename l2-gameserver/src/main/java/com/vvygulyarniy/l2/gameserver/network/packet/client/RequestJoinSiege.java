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
 * @author KenM
 */
public final class RequestJoinSiege extends L2GameClientPacket {
    private static final String _C__AD_RequestJoinSiege = "[C] AD RequestJoinSiege";

    private int _castleId;
    private int _isAttacker;
    private int _isJoining;

    public RequestJoinSiege(ByteBuffer buf) {
        super(buf);
    }

    @Override
    protected void readImpl() {
        _castleId = readD();
        _isAttacker = readD();
        _isJoining = readD();
    }

    /*@Override
    protected void runImpl() {
        L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }

        if (!activeChar.hasClanPrivilege(ClanPrivilege.CS_MANAGE_SIEGE)) {
            activeChar.send(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
            return;
        }

        L2Clan clan = activeChar.getClan();
        if (clan == null) {
            return;
        }

        Castle castle = CastleManager.getInstance().getCastleById(_castleId);
        if (castle != null) {
            if (_isJoining == 1) {
                if (System.currentTimeMillis() < clan.getDissolvingExpiryTime()) {
                    activeChar.send(SystemMessageId.CANT_PARTICIPATE_IN_SIEGE_WHILE_DISSOLUTION_IN_PROGRESS);
                    return;
                }
                if (_isAttacker == 1) {
                    castle.getSiege().registerAttacker(activeChar);
                } else {
                    castle.getSiege().registerDefender(activeChar);
                }
            } else {
                castle.getSiege().removeSiegeClan(activeChar);
            }
            castle.getSiege().listRegisterClan(activeChar);
        }

        SiegableHall hall = CHSiegeManager.getInstance().getSiegableHall(_castleId);
        if (hall != null) {
            if (_isJoining == 1) {
                if (System.currentTimeMillis() < clan.getDissolvingExpiryTime()) {
                    activeChar.send(SystemMessageId.CANT_PARTICIPATE_IN_SIEGE_WHILE_DISSOLUTION_IN_PROGRESS);
                    return;
                }
                CHSiegeManager.getInstance().registerClan(clan, hall, activeChar);
            } else {
                CHSiegeManager.getInstance().unRegisterClan(clan, hall);
            }
            activeChar.send(new SiegeInfo(hall));
        }
    }*/


    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}