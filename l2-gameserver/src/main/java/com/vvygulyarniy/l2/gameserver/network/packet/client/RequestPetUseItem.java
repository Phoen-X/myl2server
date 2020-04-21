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

import java.nio.ByteBuffer;

public final class RequestPetUseItem extends L2GameClientPacket {
    private static final String _C__8A_REQUESTPETUSEITEM = "[C] 8A RequestPetUseItem";

    private int _objectId;

    public RequestPetUseItem(ByteBuffer buf) {
        super(buf);
    }

    @Override
    protected void readImpl() {
        _objectId = readD();
        // TODO: implement me properly
        // readQ();
        // readD();
    }

    /*  @Override
      protected void runImpl() {
          final L2PcInstance activeChar = getClient().getActiveChar();
          if ((activeChar == null) || !activeChar.hasPet()) {
              return;
          }

          if (!getClient().getFloodProtectors().getUseItem().tryPerformAction("pet use item")) {
              return;
          }

          final L2PetInstance pet = (L2PetInstance) activeChar.getSummon();
          final L2ItemInstance item = pet.getInventory().getItemByObjectId(_objectId);
          if (item == null) {
              return;
          }

          if (!item.getItem().isForNpc()) {
              activeChar.send(SystemMessageId.PET_CANNOT_USE_ITEM);
              return;
          }

          if (activeChar.isAlikeDead() || pet.isDead()) {
              final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED);
              sm.addItemName(item);
              activeChar.send(sm);
              return;
          }

          // If the item has reuse time and it has not passed.
          // Message from reuse delay must come from item.
          final int reuseDelay = item.getReuseDelay();
          if (reuseDelay > 0) {
              final long reuse = pet.getItemRemainingReuseTime(item.getObjectId());
              if (reuse > 0) {
                  return;
              }
          }

          if (!item.isEquipped() && !item.getItem().checkCondition(pet, pet, true)) {
              return;
          }

          useItem(pet, item, activeChar);
      }*/
/*
    private void useItem(L2PetInstance pet, L2ItemInstance item, L2PcInstance activeChar) {
        if (item.isEquipable()) {
            if (!item.getItem().isConditionAttached()) {
                activeChar.send(SystemMessageId.PET_CANNOT_USE_ITEM);
                return;
            }

            if (item.isEquipped()) {
                pet.getInventory().unEquipItemInSlot(item.getLocationSlot());
            } else {
                pet.getInventory().equipItem(item);
            }

            activeChar.send(new PetItemList(pet.getInventory().getItems()));
            pet.updateAndBroadcastStatus(1);
        } else {
            final IItemHandler handler = ItemHandler.getInstance().getHandler(item.getEtcItem());
            if (handler != null) {
                if (handler.useItem(pet, item, false)) {
                    final int reuseDelay = item.getReuseDelay();
                    if (reuseDelay > 0) {
                        activeChar.addTimeStampItem(item, reuseDelay);
                    }
                    pet.updateAndBroadcastStatus(1);
                }
            } else {
                activeChar.send(SystemMessageId.PET_CANNOT_USE_ITEM);
                _log.warning("No item handler registered for itemId: " + item.getId());
            }
        }
    }
    */
    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new RuntimeException("Not implemented yet");
    }
}
