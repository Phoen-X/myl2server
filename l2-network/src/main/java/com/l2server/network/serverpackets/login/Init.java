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
package com.l2server.network.serverpackets.login;


import com.l2server.network.login.L2LoginClient;
import lombok.ToString;

import java.nio.ByteBuffer;

/**
 * <pre>
 * Format: dd b dddd s
 * d: session id
 * d: protocol revision
 * b: 0x90 bytes : 0x80 bytes for the scrambled RSA public key
 *                 0x10 bytes at 0x00
 * d: unknow
 * d: unknow
 * d: unknow
 * d: unknow
 * s: blowfish key
 * </pre>
 */
@ToString
public final class Init extends L2LoginServerPacket {
    private final int _sessionId;

    private final byte[] _publicKey;
    private final byte[] _blowfishKey;

    public Init(L2LoginClient client) {
        this(client.getScrambledModulus(), client.getBlowfishKey(), client.getSessionId());
    }

    public Init(byte[] publickey, byte[] blowfishkey, int sessionId) {
        _sessionId = sessionId;
        _publicKey = publickey;
        _blowfishKey = blowfishkey;
    }

    @Override
    public void write(ByteBuffer buffer) {
        writeC(buffer, 0x00); // init packet id
        writeD(buffer, _sessionId); // session id
        writeD(buffer, 0x0000c621); // protocol revision
        writeB(buffer, _publicKey); // RSA Public Key
        writeD(buffer, 0x29DD954E);// unk GG related?
        writeD(buffer, 0x77C39CFC);// unk GG related?
        writeD(buffer, 0x97ADB620);// unk GG related?
        writeD(buffer, 0x07BDE0F7);// unk GG related?
        writeB(buffer, _blowfishKey); // BlowFish key
        writeC(buffer, 0x00); // null termination ;)
    }
}
