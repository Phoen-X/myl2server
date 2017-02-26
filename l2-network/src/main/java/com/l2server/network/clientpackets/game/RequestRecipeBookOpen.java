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

public final class RequestRecipeBookOpen extends L2GameClientPacket {
    private static final String _C__B5_REQUESTRECIPEBOOKOPEN = "[C] B5 RequestRecipeBookOpen";

    private boolean _isDwarvenCraft;

    @Override
    protected void readImpl() {
        _isDwarvenCraft = (readD() == 0);
    }

    @Override
    public void process(GameServerPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }

    /*@Override
    protected void runImpl() {
        final L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }

        if (activeChar.isCastingNow() || activeChar.isCastingSimultaneouslyNow()) {
            activeChar.sendPacket(SystemMessageId.NO_RECIPE_BOOK_WHILE_CASTING);
            return;
        }

        if (activeChar.getActiveRequester() != null) {
            activeChar.sendMessage("You may not alter your recipe book while trading.");
            return;
        }

        RecipeController.getInstance().requestBookOpen(activeChar, _isDwarvenCraft);
    }*/
}
