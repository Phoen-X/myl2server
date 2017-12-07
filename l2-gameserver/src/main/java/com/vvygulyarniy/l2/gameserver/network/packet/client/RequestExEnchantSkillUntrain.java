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
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

/**
 * Format (ch) dd c: (id) 0xD0 h: (subid) 0x33 d: skill id d: skill lvl
 *
 * @author -Wooden-
 */
public final class RequestExEnchantSkillUntrain extends L2GameClientPacket {
    private static final String _C__D0_33_REQUESTEXENCHANTSKILLUNTRAIN = "[C] D0:33 RequestExEnchantSkillUntrain";
    private static final Logger _logEnchant = Logger.getLogger("enchant");

    private int _skillId;
    private int _skillLvl;

    public RequestExEnchantSkillUntrain(ByteBuffer buf) {
        super(buf);
    }

    @Override
    protected void readImpl() {
        _skillId = readD();
        _skillLvl = readD();
    }

    /*@Override
    protected void runImpl() {
        if ((_skillId <= 0) || (_skillLvl <= 0)) {
            return;
        }

        L2PcInstance player = getClient().getActiveChar();
        if (player == null) {
            return;
        }

        if (player.getClassId().level() < 3) // requires to have 3rd class quest completed
        {
            player.send(SystemMessageId.YOU_CANNOT_USE_SKILL_ENCHANT_IN_THIS_CLASS);
            return;
        }

        if (player.getLevel() < 76) {
            player.send(SystemMessageId.YOU_CANNOT_USE_SKILL_ENCHANT_ON_THIS_LEVEL);
            return;
        }

        if (!player.isAllowedToEnchantSkills()) {
            player.send(SystemMessageId.YOU_CANNOT_USE_SKILL_ENCHANT_ATTACKING_TRANSFORMED_BOAT);
            return;
        }

        L2EnchantSkillLearn s = EnchantSkillGroupsData.getInstance().getSkillEnchantmentBySkillId(_skillId);
        if (s == null) {
            return;
        }

        if ((_skillLvl % 100) == 0) {
            _skillLvl = s.getBaseLevel();
        }

        Skill skill = SkillData.getInstance().getSkill(_skillId, _skillLvl);
        if (skill == null) {
            return;
        }

        int reqItemId = EnchantSkillGroupsData.UNTRAIN_ENCHANT_BOOK;

        final int beforeUntrainSkillLevel = player.getSkillLevel(_skillId);
        if (((beforeUntrainSkillLevel - 1) != _skillLvl) && (((beforeUntrainSkillLevel % 100) != 1) || (_skillLvl != s.getBaseLevel()))) {
            return;
        }

        EnchantSkillHolder esd = s.getEnchantSkillHolder(beforeUntrainSkillLevel);

        int requiredSp = esd.getSpCost();
        int requireditems = esd.getAdenaCost();

        L2ItemInstance spb = player.getInventory().getItemByItemId(reqItemId);
        if (Config.ES_SP_BOOK_NEEDED) {
            if (spb == null) // Haven't spellbook
            {
                player.send(SystemMessageId.YOU_DONT_HAVE_ALL_OF_THE_ITEMS_NEEDED_TO_ENCHANT_THAT_SKILL);
                return;
            }
        }

        if (player.getInventory().getAdena() < requireditems) {
            player.send(SystemMessageId.YOU_DONT_HAVE_ALL_OF_THE_ITEMS_NEEDED_TO_ENCHANT_THAT_SKILL);
            return;
        }

        boolean check = true;
        if (Config.ES_SP_BOOK_NEEDED) {
            check &= player.destroyItem("Consume", spb.getObjectId(), 1, player, true);
        }

        check &= player.destroyItemByItemId("Consume", Inventory.ADENA_ID, requireditems, player, true);

        if (!check) {
            player.send(SystemMessageId.YOU_DONT_HAVE_ALL_OF_THE_ITEMS_NEEDED_TO_ENCHANT_THAT_SKILL);
            return;
        }

        player.getStat().addSp((int) (requiredSp * 0.8));

        if (Config.LOG_SKILL_ENCHANTS) {
            LogRecord record = new LogRecord(Level.INFO, "Untrain");
            record.setParameters(new Object[]
                    {
                            player,
                            skill,
                            spb
                    });
            record.setLoggerName("skill");
            _logEnchant.log(record);
        }

        player.addSkill(skill, true);
        player.send(ExEnchantSkillResult.valueOf(true));

        if (Config.DEBUG) {
            _log.fine("Learned skill ID: " + _skillId + " Level: " + _skillLvl + " for " + requiredSp + " SP, " + requireditems + " Adena.");
        }

        player.send(new UserInfo(player));
        player.send(new ExBrExtraUserInfo(player));

        if (_skillLvl > 100) {
            SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.UNTRAIN_SUCCESSFUL_SKILL_S1_ENCHANT_LEVEL_DECREASED_BY_ONE);
            sm.addSkillName(_skillId);
            player.send(sm);
        } else {
            SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.UNTRAIN_SUCCESSFUL_SKILL_S1_ENCHANT_LEVEL_RESETED);
            sm.addSkillName(_skillId);
            player.send(sm);
        }
        player.sendSkillList();
        final int afterUntrainSkillLevel = player.getSkillLevel(_skillId);
        player.send(new ExEnchantSkillInfo(_skillId, afterUntrainSkillLevel));
        player.send(new ExEnchantSkillInfoDetail(2, _skillId, afterUntrainSkillLevel - 1, player));
        player.updateShortCuts(_skillId, afterUntrainSkillLevel);
    }
    */
    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}
