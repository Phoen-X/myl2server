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

import com.vvygulyarniy.l2.domain.skill.SkillType;
import com.vvygulyarniy.l2.gameserver.network.L2GameClient;
import com.vvygulyarniy.l2.gameserver.network.packet.L2ClientPacketProcessor;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Request Acquire Skill client packet implementation.
 *
 * @author Zoey76
 */
public final class RequestAcquireSkill extends L2GameClientPacket {
    private static final String _C__7C_REQUESTACQUIRESKILL = "[C] 7C RequestAcquireSkill";
    private static final String[] QUEST_VAR_NAMES =
            {
                    "EmergentAbility65-",
                    "EmergentAbility70-",
                    "ClassAbility75-",
                    "ClassAbility80-"
            };

    private int _id;
    private int _level;
    private SkillType _skillType;
    private int _subType;

    @Override
    protected void readImpl() {
        _id = readD();
        _level = readD();
        _skillType = SkillType.getSkillType(readD());
        if (_skillType == SkillType.SUBPLEDGE) {
            _subType = readD();
        }
    }

    /*
        public static void showSubUnitSkillList(L2PcInstance activeChar) {
            final List<L2SkillLearn> skills = SkillTreesData.getInstance().getAvailableSubPledgeSkills(activeChar.getClan());
            final AcquireSkillList asl = new AcquireSkillList(AcquireSkillType.SUBPLEDGE);
            int count = 0;

            for (L2SkillLearn s : skills) {
                if (SkillData.getInstance().getSkill(s.getSkillId(), s.getSkillLevel()) != null) {
                    asl.addSkill(s.getSkillId(), s.getSkillLevel(), s.getSkillLevel(), s.getLevelUpSp(), 0);
                    ++count;
                }
            }

            if (count == 0) {
                activeChar.send(SystemMessageId.NO_MORE_SKILLS_TO_LEARN);
            } else {
                activeChar.send(asl);
            }
        }

        public static boolean canTransform(L2PcInstance player) {
            if (Config.ALLOW_TRANSFORM_WITHOUT_QUEST) {
                return true;
            }
            final QuestState st = player.getQuestState("Q00136_MoreThanMeetsTheEye");
            return (st != null) && st.isCompleted();
        }

        */
    /*@Override
    protected void runImpl() {
        final L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }

        if ((_level < 1) || (_level > 1000) || (_id < 1) || (_id > 32000)) {
            Util.handleIllegalPlayerAction(activeChar, "Wrong Packet Data in Aquired Skill", Config.DEFAULT_PUNISH);
            _log.warning("Recived Wrong Packet Data in Aquired Skill - id: " + _id + " level: " + _level + " for " + activeChar);
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
            _log.warning(RequestAcquireSkill.class.getSimpleName() + ": Player " + activeChar.getName() + " is trying to learn a null skill Id: " + _id + " level: " + _level + "!");
            return;
        }

        // Hack check. Doesn't apply to all Skill Types
        final int prevSkillLevel = activeChar.getSkillLevel(_id);
        if ((prevSkillLevel > 0) && !((_skillType == AcquireSkillType.TRANSFER) || (_skillType == AcquireSkillType.SUBPLEDGE))) {
            if (prevSkillLevel == _level) {
                _log.warning("Player " + activeChar.getName() + " is trying to learn a skill that already knows, Id: " + _id + " level: " + _level + "!");
                return;
            } else if (prevSkillLevel != (_level - 1)) {
                // The previous level skill has not been learned.
                activeChar.send(SystemMessageId.PREVIOUS_LEVEL_SKILL_NOT_LEARNED);
                Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " is requesting skill Id: " + _id + " level " + _level + " without knowing it's previous level!", IllegalActionPunishmentType.NONE);
                return;
            }
        }

        final L2SkillLearn s = SkillTreesData.getInstance().getSkillLearn(_skillType, _id, _level, activeChar);
        if (s == null) {
            return;
        }

        switch (_skillType) {
            case CLASS: {
                if (checkPlayerSkill(activeChar, trainer, s)) {
                    giveSkill(activeChar, trainer, skill);
                }
                break;
            }
            case TRANSFORM: {
                // Hack check.
                if (!canTransform(activeChar)) {
                    activeChar.send(SystemMessageId.NOT_COMPLETED_QUEST_FOR_SKILL_ACQUISITION);
                    Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " is requesting skill Id: " + _id + " level " + _level + " without required quests!", IllegalActionPunishmentType.NONE);
                    return;
                }

                if (checkPlayerSkill(activeChar, trainer, s)) {
                    giveSkill(activeChar, trainer, skill);
                }
                break;
            }
            case FISHING: {
                if (checkPlayerSkill(activeChar, trainer, s)) {
                    giveSkill(activeChar, trainer, skill);
                }
                break;
            }
            case PLEDGE: {
                if (!activeChar.isClanLeader()) {
                    return;
                }

                final L2Clan clan = activeChar.getClan();
                int repCost = s.getLevelUpSp();
                if (clan.getReputationScore() >= repCost) {
                    if (Config.LIFE_CRYSTAL_NEEDED) {
                        for (ItemHolder item : s.getRequiredItems()) {
                            if (!activeChar.destroyItemByItemId("Consume", item.getId(), item.getCount(), trainer, false)) {
                                // Doesn't have required item.
                                activeChar.send(SystemMessageId.ITEM_OR_PREREQUISITES_MISSING_TO_LEARN_SKILL);
                                L2VillageMasterInstance.showPledgeSkillList(activeChar);
                                return;
                            }

                            final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S2_S1_DISAPPEARED);
                            sm.addItemName(item.getId());
                            sm.addLong(item.getCount());
                            activeChar.send(sm);
                        }
                    }

                    clan.takeReputationScore(repCost, true);

                    final SystemMessage cr = SystemMessage.getSystemMessage(SystemMessageId.S1_DEDUCTED_FROM_CLAN_REP);
                    cr.addInt(repCost);
                    activeChar.send(cr);

                    clan.addNewSkill(skill);

                    clan.broadcastToOnlineMembers(new PledgeSkillList(clan));

                    activeChar.send(new AcquireSkillDone());

                    L2VillageMasterInstance.showPledgeSkillList(activeChar);
                } else {
                    activeChar.send(SystemMessageId.ACQUIRE_SKILL_FAILED_BAD_CLAN_REP_SCORE);
                    L2VillageMasterInstance.showPledgeSkillList(activeChar);
                }
                break;
            }
            case SUBPLEDGE: {
                if (!activeChar.isClanLeader() || !activeChar.hasClanPrivilege(ClanPrivilege.CL_TROOPS_FAME)) {
                    return;
                }

                final L2Clan clan = activeChar.getClan();
                if ((clan.getFortId() == 0) && (clan.getCastleId() == 0)) {
                    return;
                }

                // Hack check. Check if SubPledge can accept the new skill:
                if (!clan.isLearnableSubPledgeSkill(skill, _subType)) {
                    activeChar.send(SystemMessageId.SQUAD_SKILL_ALREADY_ACQUIRED);
                    Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " is requesting skill Id: " + _id + " level " + _level + " without knowing it's previous level!", IllegalActionPunishmentType.NONE);
                    return;
                }

                final int repCost = s.getLevelUpSp();
                if (clan.getReputationScore() < repCost) {
                    activeChar.send(SystemMessageId.ACQUIRE_SKILL_FAILED_BAD_CLAN_REP_SCORE);
                    return;
                }

                for (ItemHolder item : s.getRequiredItems()) {
                    if (!activeChar.destroyItemByItemId("SubSkills", item.getId(), item.getCount(), trainer, false)) {
                        activeChar.send(SystemMessageId.ITEM_OR_PREREQUISITES_MISSING_TO_LEARN_SKILL);
                        return;
                    }

                    final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S2_S1_DISAPPEARED);
                    sm.addItemName(item.getId());
                    sm.addLong(item.getCount());
                    activeChar.send(sm);
                }

                if (repCost > 0) {
                    clan.takeReputationScore(repCost, true);
                    final SystemMessage cr = SystemMessage.getSystemMessage(SystemMessageId.S1_DEDUCTED_FROM_CLAN_REP);
                    cr.addInt(repCost);
                    activeChar.send(cr);
                }

                clan.addNewSkill(skill, _subType);
                clan.broadcastToOnlineMembers(new PledgeSkillList(clan));
                activeChar.send(new AcquireSkillDone());

                showSubUnitSkillList(activeChar);
                break;
            }
            case TRANSFER: {
                if (checkPlayerSkill(activeChar, trainer, s)) {
                    giveSkill(activeChar, trainer, skill);
                }
                break;
            }
            case SUBCLASS: {
                // Hack check.
                if (activeChar.isSubClassActive()) {
                    activeChar.send(SystemMessageId.SKILL_NOT_FOR_SUBCLASS);
                    Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " is requesting skill Id: " + _id + " level " + _level + " while Sub-Class is active!", IllegalActionPunishmentType.NONE);
                    return;
                }

                // Certification Skills - Exploit fix
                if ((prevSkillLevel == -1) && (_level > 1)) {
                    // The previous level skill has not been learned.
                    activeChar.send(SystemMessageId.PREVIOUS_LEVEL_SKILL_NOT_LEARNED);
                    Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " is requesting skill Id: " + _id + " level " + _level + " without knowing it's previous level!", IllegalActionPunishmentType.NONE);
                    return;
                }

                QuestState st = activeChar.getQuestState("SubClassSkills");
                if (st == null) {
                    final Quest subClassSkilllsQuest = QuestManager.getInstance().getQuest("SubClassSkills");
                    if (subClassSkilllsQuest != null) {
                        st = subClassSkilllsQuest.newQuestState(activeChar);
                    } else {
                        _log.warning("Null SubClassSkills quest, for Sub-Class skill Id: " + _id + " level: " + _level + " for player " + activeChar.getName() + "!");
                        return;
                    }
                }

                for (String varName : QUEST_VAR_NAMES) {
                    for (int i = 1; i <= Config.MAX_SUBCLASS; i++) {
                        final String itemOID = st.getGlobalQuestVar(varName + i);
                        if (!itemOID.isEmpty() && !itemOID.endsWith(";") && !itemOID.equals("0")) {
                            if (Util.isDigit(itemOID)) {
                                final int itemObjId = Integer.parseInt(itemOID);
                                final L2ItemInstance item = activeChar.getInventory().getItemByObjectId(itemObjId);
                                if (item != null) {
                                    for (ItemHolder itemIdCount : s.getRequiredItems()) {
                                        if (item.getId() == itemIdCount.getId()) {
                                            if (checkPlayerSkill(activeChar, trainer, s)) {
                                                giveSkill(activeChar, trainer, skill);
                                                // Logging the given skill.
                                                st.saveGlobalQuestVar(varName + i, skill.getId() + ";");
                                            }
                                            return;
                                        }
                                    }
                                } else {
                                    _log.warning("Inexistent item for object Id " + itemObjId + ", for Sub-Class skill Id: " + _id + " level: " + _level + " for player " + activeChar.getName() + "!");
                                }
                            } else {
                                _log.warning("Invalid item object Id " + itemOID + ", for Sub-Class skill Id: " + _id + " level: " + _level + " for player " + activeChar.getName() + "!");
                            }
                        }
                    }
                }

                // Player doesn't have required item.
                activeChar.send(SystemMessageId.ITEM_OR_PREREQUISITES_MISSING_TO_LEARN_SKILL);
                showSkillList(trainer, activeChar);
                break;
            }
            case COLLECT: {
                if (checkPlayerSkill(activeChar, trainer, s)) {
                    giveSkill(activeChar, trainer, skill);
                }
                break;
            }
            default: {
                _log.warning("Recived Wrong Packet Data in Aquired Skill, unknown skill type:" + _skillType);
                break;
            }
        }
    }
*/
    /*private boolean checkPlayerSkill(L2PcInstance player, L2Npc trainer, L2SkillLearn s) {
        if (s != null) {
            if ((s.getSkillId() == _id) && (s.getSkillLevel() == _level)) {
                // Hack check.
                if (s.getGetLevel() > player.getLevel()) {
                    player.send(SystemMessageId.YOU_DONT_MEET_SKILL_LEVEL_REQUIREMENTS);
                    Util.handleIllegalPlayerAction(player, "Player " + player.getName() + ", level " + player.getLevel() + " is requesting skill Id: " + _id + " level " + _level + " without having minimum required level, " + s.getGetLevel() + "!", IllegalActionPunishmentType.NONE);
                    return false;
                }

                // First it checks that the skill require SP and the player has enough SP to learn it.
                final int levelUpSp = s.getCalculatedLevelUpSp(player.getClassId(), player.getLearningClass());
                if ((levelUpSp > 0) && (levelUpSp > player.getSp())) {
                    player.send(SystemMessageId.NOT_ENOUGH_SP_TO_LEARN_SKILL);
                    showSkillList(trainer, player);
                    return false;
                }

                if (!Config.DIVINE_SP_BOOK_NEEDED && (_id == CommonSkill.DIVINE_INSPIRATION.getId())) {
                    return true;
                }

                // Check for required skills.
                if (!s.getPreReqSkills().isEmpty()) {
                    for (SkillHolder skill : s.getPreReqSkills()) {
                        if (player.getSkillLevel(skill.getSkillId()) != skill.getSkillLvl()) {
                            if (skill.getSkillId() == CommonSkill.ONYX_BEAST_TRANSFORMATION.getId()) {
                                player.send(SystemMessageId.YOU_MUST_LEARN_ONYX_BEAST_SKILL);
                            } else {
                                player.send(SystemMessageId.ITEM_OR_PREREQUISITES_MISSING_TO_LEARN_SKILL);
                            }
                            return false;
                        }
                    }
                }

                // Check for required items.
                if (!s.getRequiredItems().isEmpty()) {
                    // Then checks that the player has all the items
                    long reqItemCount = 0;
                    for (ItemHolder item : s.getRequiredItems()) {
                        reqItemCount = player.getInventory().getInventoryItemCount(item.getId(), -1);
                        if (reqItemCount < item.getCount()) {
                            // Player doesn't have required item.
                            player.send(SystemMessageId.ITEM_OR_PREREQUISITES_MISSING_TO_LEARN_SKILL);
                            showSkillList(trainer, player);
                            return false;
                        }
                    }
                    // If the player has all required items, they are consumed.
                    for (ItemHolder itemIdCount : s.getRequiredItems()) {
                        if (!player.destroyItemByItemId("SkillLearn", itemIdCount.getId(), itemIdCount.getCount(), trainer, true)) {
                            Util.handleIllegalPlayerAction(player, "Somehow player " + player.getName() + ", level " + player.getLevel() + " lose required item Id: " + itemIdCount.getId() + " to learn skill while learning skill Id: " + _id + " level " + _level + "!", IllegalActionPunishmentType.NONE);
                        }
                    }
                }
                // If the player has SP and all required items then consume SP.
                if (levelUpSp > 0) {
                    player.setSp(player.getSp() - levelUpSp);
                    final StatusUpdate su = new StatusUpdate(player);
                    su.addAttribute(StatusUpdate.SP, player.getSp());
                    player.send(su);
                }
                return true;
            }
        }
        return false;
    }

    private void giveSkill(L2PcInstance player, L2Npc trainer, Skill skill) {
        // Send message.
        final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.LEARNED_SKILL_S1);
        sm.addSkillName(skill);
        player.send(sm);

        player.send(new AcquireSkillDone());

        player.addSkill(skill, true);
        player.sendSkillList();

        player.updateShortCuts(_id, _level);
        showSkillList(trainer, player);

        // If skill is expand type then sends packet:
        if ((_id >= 1368) && (_id <= 1372)) {
            player.send(new ExStorageMaxCount(player));
        }

        // Notify scripts of the skill learn.
        EventDispatcher.getInstance().notifyEventAsync(new OnPlayerSkillLearn(trainer, player, skill, _skillType), trainer);
    }
    */
    /*private void showSkillList(L2Npc trainer, L2PcInstance player) {
        if ((_skillType == AcquireSkillType.TRANSFORM) || (_skillType == AcquireSkillType.SUBCLASS) || (_skillType == AcquireSkillType.TRANSFER)) {
            // Managed in Datapack.
            return;
        }

        if (trainer instanceof L2FishermanInstance) {
            L2FishermanInstance.showFishSkillList(player);
        } else {
            L2NpcInstance.showSkillList(player, trainer, player.getLearningClass());
        }
    }
    */
    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }

}