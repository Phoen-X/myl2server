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
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * @author JIV
 */
public final class EndScenePlayer extends L2GameClientPacket {
    private static final String _C__D0_5B_ENDSCENEPLAYER = "[C] D0:5B EndScenePlayer";

    private int _movieId;

    public EndScenePlayer(@NotNull ByteBuffer buffer) {
        super(buffer);
    }

    @Override
    protected void readImpl() {
        _movieId = readD();
    }

    /*@Override
    protected void runImpl() {
        L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }
        if (_movieId == 0) {
            return;
        }
        if (activeChar.getMovieId() != _movieId) {
            _log.warning("Player " + getClient() + " sent EndScenePlayer with wrong movie id: " + _movieId);
            return;
        }
        activeChar.setMovieId(0);
        activeChar.setIsTeleporting(true, false); // avoid to get player removed from L2World
        activeChar.decayMe();
        activeChar.spawnMe(activeChar.getX(), activeChar.getY(), activeChar.getZ());
        activeChar.setIsTeleporting(false, false);
    }
    */
    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new RuntimeException("Not implemented yet");
    }
}
