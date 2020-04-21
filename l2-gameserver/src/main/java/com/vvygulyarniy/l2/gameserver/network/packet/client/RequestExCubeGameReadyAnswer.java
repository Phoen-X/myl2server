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
 * Format: chddd d: Arena d: Answer
 *
 * @author mrTJO
 */
public final class RequestExCubeGameReadyAnswer extends L2GameClientPacket {
    private static final String _C__D0_5C_REQUESTEXCUBEGAMEREADYANSWER = "[C] D0:5C RequestExCubeGameReadyAnswer";

    private int _arena;
    private int _answer;

    public RequestExCubeGameReadyAnswer(@NotNull ByteBuffer buffer) {
        super(buffer);
    }

    @Override
    protected void readImpl() {
        // client sends -1,0,1,2 for arena parameter
        _arena = readD() + 1;
        // client sends 1 if clicked confirm on not clicked, 0 if clicked cancel
        _answer = readD();
    }

    /*@Override
    public void runImpl() {
        L2PcInstance player = getClient().getActiveChar();

        if (player == null) {
            return;
        }

        switch (_answer) {
            case 0:
                // Cancel - Answer No
                break;
            case 1:
                // OK or Time Over
                HandysBlockCheckerManager.getInstance().increaseArenaVotes(_arena);
                break;
            default:
                _log.warning("Unknown Cube Game Answer ID: " + _answer);
                break;
        }
    }
    */
    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new RuntimeException("Not implemented yet");
    }
}
