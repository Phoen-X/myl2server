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
 * (ch)cS{S} c: change pass? S: current password S: new password
 *
 * @author mrTJO
 */
public class RequestEx2ndPasswordReq extends L2GameClientPacket {
    private static final String _C__D0_AF_REQUESTEX2NDPASSWORDREQ = "[C] D0:AF RequestEx2ndPasswordReq";

    private int _changePass;
    private String _password, _newPassword;

    @Override
    protected void readImpl() {
        _changePass = readC();
        _password = readS();
        if (_changePass == 2) {
            _newPassword = readS();
        }
    }

    /*@Override
    protected void runImpl() {
        if (!SecondaryAuthData.getInstance().isEnabled()) {
            return;
        }

        SecondaryPasswordAuth spa = getClient().getSecondaryAuth();
        boolean exVal = false;

        if ((_changePass == 0) && !spa.passwordExist()) {
            exVal = spa.savePassword(_password);
        } else if ((_changePass == 2) && spa.passwordExist()) {
            exVal = spa.changePassword(_password, _newPassword);
        }

        if (exVal) {
            getClient().sendPacket(new Ex2ndPasswordAck(Ex2ndPasswordAck.SUCCESS));
        }
    }
    */
    @Override
    public void process(GameServerPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}