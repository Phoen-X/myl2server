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
package com.l2server.network.clientpackets;


import com.l2server.network.ClientPacketProcessor;
import com.l2server.network.L2LoginClient;
import com.l2server.network.ReceivablePacket;

/**
 * @author KenM
 */
public abstract class L2LoginClientPacket extends ReceivablePacket {

    @Override
    public final boolean read() {
        try {
            return readImpl();
        } catch (Exception e) {

            return false;
        }
    }

    protected abstract boolean readImpl();

    public abstract void process(ClientPacketProcessor processor, L2LoginClient client);
}
