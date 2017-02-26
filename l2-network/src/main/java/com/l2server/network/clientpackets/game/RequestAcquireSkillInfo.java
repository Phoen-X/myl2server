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
import com.vvygulyarniy.l2.domain.skill.SkillType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Request Acquire Skill Info client packet implementation.
 *
 * @author Zoey76
 */
public final class RequestAcquireSkillInfo extends L2GameClientPacket {
    private static final String _C__73_REQUESTACQUIRESKILLINFO = "[C] 73 RequestAcquireSkillInfo";

    private int _id;
    private int _level;
    private SkillType _skillType;

    @Override
    protected void readImpl() {
        _id = readD();
        _level = readD();
        _skillType = SkillType.getSkillType(readD());
    }

    /*@Override
    protected void runImpl() {
        if ((_id <= 0) || (_level <= 0)) {
            _log.warning(RequestAcquireSkillInfo.class.getSimpleName() + ": Invalid Id: " + _id + " or level: " + _level + "!");
            return;
        }

        final L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }

        final L2Npc trainer = activeChar.getLastFolkNPC();
        if (!(trainer instanceof L2NpcInstance)) {
            return;
        }

        if (!trainer.canInteract(activeChar) && !activeChar.isGM()) {
            return;
        }

        final Skill skill = SkillData.getInstance().getSkill(_id, _level);
        if (skill == null) {
            _log.warning(RequestAcquireSkillInfo.class.getSimpleName() + ": Skill Id: " + _id + " level: " + _level + " is undefined. " + RequestAcquireSkillInfo.class.getName() + " failed.");
            return;
        }

        // Hack check. Doesn't apply to all Skill Types
        final int prevSkillLevel = activeChar.getSkillLevel(_id);
        if ((prevSkillLevel > 0) && !((_skillType == AcquireSkillType.TRANSFER) || (_skillType == AcquireSkillType.SUBPLEDGE))) {
            if (prevSkillLevel == _level) {
                _log.warning(RequestAcquireSkillInfo.class.getSimpleName() + ": Player " + activeChar.getName() + " is trequesting info for a skill that already knows, Id: " + _id + " level: " + _level + "!");
            } else if (prevSkillLevel != (_level - 1)) {
                _log.warning(RequestAcquireSkillInfo.class.getSimpleName() + ": Player " + activeChar.getName() + " is requesting info for skill Id: " + _id + " level " + _level + " without knowing it's previous level!");
            }
        }

        final L2SkillLearn s = SkillTreesData.getInstance().getSkillLearn(_skillType, _id, _level, activeChar);
        if (s == null) {
            return;
        }

        switch (_skillType) {
            case TRANSFORM:
            case FISHING:
            case SUBCLASS:
            case COLLECT:
            case TRANSFER: {
                sendPacket(new AcquireSkillInfo(_skillType, s));
                break;
            }
            case CLASS: {
                if (trainer.getTemplate().canTeach(activeChar.getLearningClass())) {
                    final int customSp = s.getCalculatedLevelUpSp(activeChar.getClassId(), activeChar.getLearningClass());
                    sendPacket(new AcquireSkillInfo(_skillType, s, customSp));
                }
                break;
            }
            case PLEDGE: {
                if (!activeChar.isClanLeader()) {
                    return;
                }
                sendPacket(new AcquireSkillInfo(_skillType, s));
                break;
            }
            case SUBPLEDGE: {
                if (!activeChar.isClanLeader() || !activeChar.hasClanPrivilege(ClanPrivilege.CL_TROOPS_FAME)) {
                    return;
                }
                sendPacket(new AcquireSkillInfo(_skillType, s));
                break;
            }
        }
    }*/


    @Override
    public void process(GameServerPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}
