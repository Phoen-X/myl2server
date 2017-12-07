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
import lombok.ToString;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.nio.ByteBuffer;

/**
 * Format chS c (id) 0xD0 h (subid) 0x0C S the hero's words :)
 *
 * @author -Wooden-
 */
@ToString
public final class RequestWriteHeroWords extends L2GameClientPacket {
    private static final String _C__D0_05_REQUESTWRITEHEROWORDS = "[C] D0:05 RequestWriteHeroWords";

    private String _heroWords;

    public RequestWriteHeroWords(ByteBuffer buf) {
        super(buf);
    }

    @Override
    protected void readImpl() {
        _heroWords = readS();
    }

    /*@Override
    protected void runImpl() {
        final L2PcInstance player = getClient().getActiveChar();
        if ((player == null) || !player.isHero()) {
            return;
        }

        if ((_heroWords == null) || (_heroWords.length() > 300)) {
            return;
        }

        Hero.getInstance().setHeroMessage(player, _heroWords);
    }
    */
    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}