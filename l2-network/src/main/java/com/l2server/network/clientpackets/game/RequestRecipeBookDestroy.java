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

public final class RequestRecipeBookDestroy extends L2GameClientPacket {
    private static final String _C__B6_REQUESTRECIPEBOOKDESTROY = "[C] B6 RequestRecipeBookDestroy";

    private int _recipeID;

    /**
     * Unknown Packet:ad 0000: ad 02 00 00 00
     */
    @Override
    protected void readImpl() {
        _recipeID = readD();
    }

    /*@Override
    protected void runImpl() {
        final L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }

        if (!getClient().getFloodProtectors().getTransaction().tryPerformAction("RecipeDestroy")) {
            return;
        }

        final L2RecipeList rp = RecipeData.getInstance().getRecipeList(_recipeID);
        if (rp == null) {
            return;
        }
        activeChar.unregisterRecipeList(_recipeID);

        RecipeBookItemList response = new RecipeBookItemList(rp.isDwarvenRecipe(), activeChar.getMaxMp());
        if (rp.isDwarvenRecipe()) {
            response.addRecipes(activeChar.getDwarvenRecipeBook());
        } else {
            response.addRecipes(activeChar.getCommonRecipeBook());
        }

        activeChar.sendPacket(response);
    }
    */
    @Override
    public void process(GameServerPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}