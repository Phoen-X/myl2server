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

public final class RequestEnchantItem extends L2GameClientPacket {
    protected static final Logger _logEnchant = Logger.getLogger("enchant");

    private static final String _C__5F_REQUESTENCHANTITEM = "[C] 5F RequestEnchantItem";

    private int _objectId;
    private int _supportId;

    public RequestEnchantItem(ByteBuffer buf) {
        super(buf);
    }

    @Override
    protected void readImpl() {
        _objectId = readD();
        _supportId = readD();
    }

    /*@Override
    protected void runImpl() {
        final L2PcInstance activeChar = getClient().getActiveChar();
        if ((activeChar == null) || (_objectId == 0)) {
            return;
        }

        if (!activeChar.isOnline() || getClient().isDetached()) {
            activeChar.setActiveEnchantItemId(L2PcInstance.ID_NONE);
            return;
        }

        if (activeChar.isProcessingTransaction() || activeChar.isInStoreMode()) {
            activeChar.send(SystemMessageId.CANNOT_ENCHANT_WHILE_STORE);
            activeChar.setActiveEnchantItemId(L2PcInstance.ID_NONE);
            return;
        }

        L2ItemInstance item = activeChar.getInventory().getItemByObjectId(_objectId);
        L2ItemInstance scroll = activeChar.getInventory().getItemByObjectId(activeChar.getActiveEnchantItemId());
        L2ItemInstance support = activeChar.getInventory().getItemByObjectId(activeChar.getActiveEnchantSupportItemId());

        if ((item == null) || (scroll == null)) {
            activeChar.setActiveEnchantItemId(L2PcInstance.ID_NONE);
            return;
        }

        // template for scroll
        final EnchantScroll scrollTemplate = EnchantItemData.getInstance().getEnchantScroll(scroll);

        // scroll not found in list
        if (scrollTemplate == null) {
            return;
        }

        // template for support item, if exist
        EnchantSupportItem supportTemplate = null;
        if (support != null) {
            if (support.getObjectId() != _supportId) {
                activeChar.setActiveEnchantItemId(L2PcInstance.ID_NONE);
                return;
            }
            supportTemplate = EnchantItemData.getInstance().getSupportItem(support);
        }

        // first validation check
        if (!scrollTemplate.isValid(item, supportTemplate)) {
            activeChar.send(SystemMessageId.INAPPROPRIATE_ENCHANT_CONDITION);
            activeChar.setActiveEnchantItemId(L2PcInstance.ID_NONE);
            activeChar.send(new EnchantResult(2, 0, 0));
            return;
        }

        // fast auto-enchant cheat check
        if ((activeChar.getActiveEnchantTimestamp() == 0) || ((System.currentTimeMillis() - activeChar.getActiveEnchantTimestamp()) < 2000)) {
            Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " use autoenchant program ", Config.DEFAULT_PUNISH);
            activeChar.setActiveEnchantItemId(L2PcInstance.ID_NONE);
            activeChar.send(new EnchantResult(2, 0, 0));
            return;
        }

        // attempting to destroy scroll
        scroll = activeChar.getInventory().destroyItem("Enchant", scroll.getObjectId(), 1, activeChar, item);
        if (scroll == null) {
            activeChar.send(SystemMessageId.NOT_ENOUGH_ITEMS);
            Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " tried to enchant with a scroll he doesn't have", Config.DEFAULT_PUNISH);
            activeChar.setActiveEnchantItemId(L2PcInstance.ID_NONE);
            activeChar.send(new EnchantResult(2, 0, 0));
            return;
        }

        // attempting to destroy support if exist
        if (support != null) {
            support = activeChar.getInventory().destroyItem("Enchant", support.getObjectId(), 1, activeChar, item);
            if (support == null) {
                activeChar.send(SystemMessageId.NOT_ENOUGH_ITEMS);
                Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " tried to enchant with a support item he doesn't have", Config.DEFAULT_PUNISH);
                activeChar.setActiveEnchantItemId(L2PcInstance.ID_NONE);
                activeChar.send(new EnchantResult(2, 0, 0));
                return;
            }
        }

        final InventoryUpdate iu = new InventoryUpdate();
        synchronized (item) {
            // last validation check
            if ((item.getOwnerId() != activeChar.getObjectId()) || (item.isEnchantable() == 0)) {
                activeChar.send(SystemMessageId.INAPPROPRIATE_ENCHANT_CONDITION);
                activeChar.setActiveEnchantItemId(L2PcInstance.ID_NONE);
                activeChar.send(new EnchantResult(2, 0, 0));
                return;
            }

            final EnchantResultType resultType = scrollTemplate.calculateSuccess(activeChar, item, supportTemplate);
            switch (resultType) {
                case ERROR: {
                    activeChar.send(SystemMessageId.INAPPROPRIATE_ENCHANT_CONDITION);
                    activeChar.setActiveEnchantItemId(L2PcInstance.ID_NONE);
                    activeChar.send(new EnchantResult(2, 0, 0));
                    break;
                }
                case SUCCESS: {
                    Skill enchant4Skill = null;
                    L2Item it = item.getItem();
                    // Increase enchant level only if scroll's base template has chance, some armors can success over +20 but they shouldn't have increased.
                    if (scrollTemplate.getChance(activeChar, item) > 0) {
                        item.setEnchantLevel(item.getEnchantLevel() + 1);
                        item.updateDatabase();
                    }
                    activeChar.send(new EnchantResult(0, 0, 0));

                    if (Config.LOG_ITEM_ENCHANTS) {
                        LogRecord record = new LogRecord(Level.INFO, "Success");
                        record.setParameters(new Object[]
                                {
                                        activeChar,
                                        item,
                                        scroll,
                                        support,
                                });
                        record.setLoggerName("item");
                        _logEnchant.log(record);
                    }

                    // announce the success
                    int minEnchantAnnounce = item.isArmor() ? 6 : 7;
                    int maxEnchantAnnounce = item.isArmor() ? 0 : 15;
                    if ((item.getEnchantLevel() == minEnchantAnnounce) || (item.getEnchantLevel() == maxEnchantAnnounce)) {
                        SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_SUCCESSFULY_ENCHANTED_A_S2_S3);
                        sm.addCharName(activeChar);
                        sm.addInt(item.getEnchantLevel());
                        sm.addItemName(item);
                        activeChar.broadcastPacket(sm);

                        Skill skill = CommonSkill.FIREWORK.getSkill();
                        if (skill != null) {
                            activeChar.broadcastPacket(new MagicSkillUse(activeChar, activeChar, skill.getId(), skill.getLevel(), skill.getHitTime(), skill.getReuseDelay()));
                        }
                    }

                    if ((item.isArmor()) && (item.getEnchantLevel() == 4) && item.isEquipped()) {
                        enchant4Skill = it.getEnchant4Skill();
                        if (enchant4Skill != null) {
                            // add skills bestowed from +4 armor
                            activeChar.addSkill(enchant4Skill, false);
                            activeChar.sendSkillList();
                        }
                    }
                    break;
                }
                case FAILURE: {
                    if (scrollTemplate.isSafe()) {
                        // safe enchant - remain old value
                        activeChar.send(SystemMessageId.SAFE_ENCHANT_FAILED);
                        activeChar.send(new EnchantResult(5, 0, 0));

                        if (Config.LOG_ITEM_ENCHANTS) {
                            LogRecord record = new LogRecord(Level.INFO, "Safe Fail");
                            record.setParameters(new Object[]
                                    {
                                            activeChar,
                                            item,
                                            scroll,
                                            support,
                                    });
                            record.setLoggerName("item");
                            _logEnchant.log(record);
                        }
                    } else {
                        // unequip item on enchant failure to avoid item skills stack
                        if (item.isEquipped()) {
                            if (item.getEnchantLevel() > 0) {
                                SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.EQUIPMENT_S1_S2_REMOVED);
                                sm.addInt(item.getEnchantLevel());
                                sm.addItemName(item);
                                activeChar.send(sm);
                            } else {
                                SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_DISARMED);
                                sm.addItemName(item);
                                activeChar.send(sm);
                            }

                            L2ItemInstance[] unequiped = activeChar.getInventory().unEquipItemInSlotAndRecord(item.getLocationSlot());
                            for (L2ItemInstance itm : unequiped) {
                                iu.addModifiedItem(itm);
                            }

                            activeChar.send(iu);
                            activeChar.broadcastUserInfo();
                        }

                        if (scrollTemplate.isBlessed()) {
                            // blessed enchant - clear enchant value
                            activeChar.send(SystemMessageId.BLESSED_ENCHANT_FAILED);

                            item.setEnchantLevel(0);
                            item.updateDatabase();
                            activeChar.send(new EnchantResult(3, 0, 0));

                            if (Config.LOG_ITEM_ENCHANTS) {
                                LogRecord record = new LogRecord(Level.INFO, "Blessed Fail");
                                record.setParameters(new Object[]
                                        {
                                                activeChar,
                                                item,
                                                scroll,
                                                support,
                                        });
                                record.setLoggerName("item");
                                _logEnchant.log(record);
                            }
                        } else {
                            // enchant failed, destroy item
                            item = activeChar.getInventory().destroyItem("Enchant", item, activeChar, null);
                            if (item == null) {
                                // unable to destroy item, cheater ?
                                Util.handleIllegalPlayerAction(activeChar, "Unable to delete item on enchant failure from player " + activeChar.getName() + ", possible cheater !", Config.DEFAULT_PUNISH);
                                activeChar.setActiveEnchantItemId(L2PcInstance.ID_NONE);
                                activeChar.send(new EnchantResult(2, 0, 0));

                                if (Config.LOG_ITEM_ENCHANTS) {
                                    LogRecord record = new LogRecord(Level.INFO, "Unable to destroy");
                                    record.setParameters(new Object[]
                                            {
                                                    activeChar,
                                                    item,
                                                    scroll,
                                                    support,
                                            });
                                    record.setLoggerName("item");
                                    _logEnchant.log(record);
                                }
                                return;
                            }

                            L2World.getInstance().removeObject(item);

                            final int crystalId = item.getItem().getCrystalItemId();
                            if ((crystalId != 0) && item.getItem().isCrystallizable()) {
                                int count = item.getCrystalCount() - ((item.getItem().getCrystalCount() + 1) / 2);
                                count = count < 1 ? 1 : count;
                                activeChar.getInventory().addItem("Enchant", crystalId, count, activeChar, item);

                                final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.EARNED_S2_S1_S);
                                sm.addItemName(crystalId);
                                sm.addLong(count);
                                activeChar.send(sm);
                                activeChar.send(new EnchantResult(1, crystalId, count));
                            } else {
                                activeChar.send(new EnchantResult(4, 0, 0));
                            }

                            if (Config.LOG_ITEM_ENCHANTS) {
                                LogRecord record = new LogRecord(Level.INFO, "Fail");
                                record.setParameters(new Object[]
                                        {
                                                activeChar,
                                                item,
                                                scroll,
                                                support,
                                        });
                                record.setLoggerName("item");
                                _logEnchant.log(record);
                            }
                        }
                    }
                    break;
                }
            }

            final StatusUpdate su = new StatusUpdate(activeChar);
            su.addAttribute(StatusUpdate.CUR_LOAD, activeChar.getCurrentLoad());
            activeChar.send(su);
            if (!Config.FORCE_INVENTORY_UPDATE) {
                if (scroll.getCount() == 0) {
                    iu.addRemovedItem(scroll);
                } else {
                    iu.addModifiedItem(scroll);
                }

                if (item.getCount() == 0) {
                    iu.addRemovedItem(item);
                } else {
                    iu.addModifiedItem(item);
                }

                if (support != null) {
                    if (support.getCount() == 0) {
                        iu.addRemovedItem(support);
                    } else {
                        iu.addModifiedItem(support);
                    }
                }

                activeChar.send(iu);
            } else {
                activeChar.send(new ItemList(activeChar, true));
            }

            activeChar.broadcastUserInfo();
            activeChar.setActiveEnchantItemId(L2PcInstance.ID_NONE);
        }
    }
    */
    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}
