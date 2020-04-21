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

public final class RequestPetGetItem extends L2GameClientPacket {
    private static final String _C__98_REQUESTPETGETITEM = "[C] 98 RequestPetGetItem";

    private int _objectId;

    public RequestPetGetItem(ByteBuffer buf) {
        super(buf);
    }

    @Override
    protected void readImpl() {
        _objectId = readD();
    }

/*
    @Override
    protected void runImpl() {
        L2World world = L2World.getInstance();
        L2ItemInstance item = (L2ItemInstance) world.findObject(_objectId);
        if ((item == null) || (getActiveChar() == null) || !getActiveChar().hasPet()) {
            send(ActionFailed.STATIC_PACKET);
            return;
        }

        final int castleId = MercTicketManager.getInstance().getTicketCastleId(item.getId());
        if (castleId > 0) {
            send(ActionFailed.STATIC_PACKET);
            return;
        }

        if (FortSiegeManager.getInstance().isCombat(item.getId())) {
            send(ActionFailed.STATIC_PACKET);
            return;
        }

        final L2PetInstance pet = (L2PetInstance) getClient().getActiveChar().getSummon();
        if (pet.isDead() || pet.isOutOfControl()) {
            send(ActionFailed.STATIC_PACKET);
            return;
        }

        if (pet.isUncontrollable()) {
            send(SystemMessageId.WHEN_YOUR_PETS_HUNGER_GAUGE_IS_AT_0_YOU_CANNOT_USE_YOUR_PET);
            return;
        }

        pet.getAI().setIntention(CtrlIntention.AI_INTENTION_PICK_UP, item);
    }
*/

    /*  @Override
      public String getType() {
          return _C__98_REQUESTPETGETITEM;
      }
  */
    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new RuntimeException("Not implemented yet");
    }
}
