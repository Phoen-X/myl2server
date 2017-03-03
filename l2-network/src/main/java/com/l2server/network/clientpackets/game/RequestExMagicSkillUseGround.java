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
 * Fromat:(ch) dddddc
 *
 * @author -Wooden-
 */
public final class RequestExMagicSkillUseGround extends L2GameClientPacket {
    private static final String _C__D0_44_REQUESTEXMAGICSKILLUSEGROUND = "[C] D0:44 RequestExMagicSkillUseGround";

    private int _x;
    private int _y;
    private int _z;
    private int _skillId;
    private boolean _ctrlPressed;
    private boolean _shiftPressed;

    @Override
    protected void readImpl() {
        _x = readD();
        _y = readD();
        _z = readD();
        _skillId = readD();
        _ctrlPressed = readD() != 0;
        _shiftPressed = readC() != 0;
    }

    /*@Override
    protected void runImpl() {
        // Get the current L2PcInstance of the player
        final L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }

        // Get the level of the used skill
        int level = activeChar.getSkillLevel(_skillId);
        if (level <= 0) {
            activeChar.send(ActionFailed.STATIC_PACKET);
            return;
        }

        // Get the L2Skill template corresponding to the skillID received from the client
        Skill skill = SkillData.getInstance().getSkill(_skillId, level);

        // Check the validity of the skill
        if (skill != null) {
            activeChar.setCurrentSkillWorldPosition(new Location(_x, _y, _z));

            // normally magicskilluse packet turns char client side but for these skills, it doesn't (even with correct target)
            activeChar.setHeading(Util.calculateHeadingFrom(activeChar.getX(), activeChar.getY(), _x, _y));
            Broadcast.toKnownPlayers(activeChar, new ValidateLocation(activeChar));

            activeChar.useMagic(skill, _ctrlPressed, _shiftPressed);
        } else {
            activeChar.send(ActionFailed.STATIC_PACKET);
            _log.warning("No skill found with id " + _skillId + " and level " + level + " !!");
        }
    }*/

    /*@Override
    public String getType() {
        return _C__D0_44_REQUESTEXMAGICSKILLUSEGROUND;
    }
*/
    @Override
    public void process(GameServerPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}
