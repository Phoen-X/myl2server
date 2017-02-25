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
package com.vvygulyarniy.l2.gameserver.network;

import com.l2server.network.*;
import com.l2server.network.clientpackets.game.L2GameClientPacket;
import com.l2server.network.clientpackets.game.ProtocolVersion;
import com.l2server.network.util.crypt.ScrambledKeyPair;

import java.nio.ByteBuffer;

/**
 * Stateful Packet Handler<BR>
 * The Stateful approach prevents the server from handling inconsistent packets, examples:<br>
 * <ul>
 * <li>Clients sends a MoveToLocation packet without having a character attached. (Potential errors handling the packet).</li>
 * <li>Clients sends a RequestAuthLogin being already authed. (Potential exploit).</li>
 * </ul>
 * Note: If for a given exception a packet needs to be handled on more then one state, then it should be added to all these states.
 *
 * @author KenM
 */
public final class L2GamePacketHandler implements IPacketHandler<L2GameClient, L2GameClientPacket>, IClientFactory<L2GameClient>, IMMOExecutor<L2GameClient> {

    // implementation
    @Override
    public L2GameClientPacket handlePacket(ByteBuffer buf, L2GameClient client) {
        if (client.dropPacket()) {
            return null;
        }

        int opcode = buf.get() & 0xFF;
        int id3;

        L2GameClientPacket msg = null;
        L2GameClient.GameClientState state = client.getState();

        switch (state) {
            case CONNECTED:
                switch (opcode) {
                    case 0x0e:
                        msg = new ProtocolVersion();
                        break;
                    /*case 0x2b:
                        msg = new AuthLogin();
						break;*/
                }
        }
        return msg;
    }

    @Override
    public void execute(ReceivablePacket packet) {

    }

    @Override
    public L2GameClient create(MMOConnection<L2GameClient> con, ScrambledKeyPair scrambledKeyPair, byte[] blowfishKey) {
        return null;
    }
}
