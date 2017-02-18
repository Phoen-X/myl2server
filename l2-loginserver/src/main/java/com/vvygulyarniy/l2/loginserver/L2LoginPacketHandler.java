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
package com.vvygulyarniy.l2.loginserver;


import com.l2server.network.IPacketHandler;
import com.l2server.network.L2LoginClient;
import com.l2server.network.clientpackets.*;

import java.nio.ByteBuffer;

/**
 * Handler for packets received by Login Server
 *
 * @author KenM
 */
public final class L2LoginPacketHandler implements IPacketHandler<L2LoginClient, L2LoginClientPacket> {

    @Override
    public L2LoginClientPacket handlePacket(ByteBuffer buf, L2LoginClient client) {
        int opcode = buf.get() & 0xFF;

        L2LoginClientPacket packet = null;
        L2LoginClient.LoginClientState state = client.getState();

        switch (state) {
            case CONNECTED:
                switch (opcode) {
                    case 0x07:
                        packet = new AuthGameGuard();
                        break;
                    default:
                        break;
                }
                break;
            case AUTHED_GG:
                switch (opcode) {
                    case 0x00:
                        packet = new RequestAuthLogin();
                        break;
                    default:
                        break;
                }
                break;
            case AUTHED_LOGIN:
                switch (opcode) {
                    case 0x02:
                        packet = new RequestServerLogin();
                        break;
                    case 0x05:
                        packet = new RequestServerList();
                        break;
                    default:
                        break;
                }
                break;
        }
        return packet;
    }


}
