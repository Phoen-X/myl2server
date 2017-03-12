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
package com.vvygulyarniy.l2.gameserver.network.packet.server;

import com.vvygulyarniy.l2.domain.geo.Point;
import com.vvygulyarniy.l2.domain.item.L2GearItem;
import com.vvygulyarniy.l2.gameserver.world.character.L2Player;
import com.vvygulyarniy.l2.gameserver.world.character.gear.PaperDoll;
import lombok.ToString;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@ToString
public final class CharSelectionInfo extends L2GameServerPacket {
    private final String loginName;
    private final int sessionId;
    private final List<L2Player> chars;
    private int activeId;

    /**
     * Constructor for CharSelectionInfo.
     *
     * @param loginName
     * @param sessionId
     */
    public CharSelectionInfo(String loginName, int sessionId, List<L2Player> chars, int activeCharId) {
        this.sessionId = sessionId;
        this.loginName = loginName;
        this.chars = chars;

        if (this.activeId == -1 && !chars.isEmpty()) {
            this.activeId = chars.get(0).getId();
        } else {
            this.activeId = activeCharId;
        }
    }

    @Override
    protected final void writeImpl(ByteBuffer buffer) {
        writeC(buffer, 0x09);
        int size = chars.size();
        writeD(buffer, size);

        // Can prevent players from creating new characters (if 0); (if 1, the client will ask if chars may be created (0x13) Response: (0x0D) )
        writeD(buffer, 3); //max char number per account
        writeC(buffer, 0x00);
        for (L2Player l2Char : chars) {
            writeS(buffer, l2Char.getName());
            writeD(buffer, l2Char.getId());
            writeS(buffer, loginName);
            writeD(buffer, sessionId);
            writeD(buffer, l2Char.getClanId());
            writeD(buffer, 0x00); // ??
            writeD(buffer, l2Char.getAppearance().getSex().ordinal());
            writeD(buffer, l2Char.getClassId().getRace().ordinal());

            writeD(buffer, l2Char.getClassId().getId());

            writeD(buffer, 0x01); // active ??
            Point position = l2Char.getPosition().getPoint();
            writeD(buffer, position.getX());
            writeD(buffer, position.getY());
            writeD(buffer, position.getZ());

            writeF(buffer, l2Char.getCurrHp());
            writeF(buffer, l2Char.getCurrMp());

            writeD(buffer, l2Char.getSp());
            writeQ(buffer, l2Char.getExp());
            writeF(buffer, 0); // High Five exp %
            writeD(buffer, l2Char.getLevel());

            writeD(buffer, 0);//l2Char.getKarma());
            writeD(buffer, 0);//l2Char.getPkKills());
            writeD(buffer, 0);//l2Char.getPvpKills());

            writeD(buffer, 0x00);
            writeD(buffer, 0x00);
            writeD(buffer, 0x00);
            writeD(buffer, 0x00);
            writeD(buffer, 0x00);
            writeD(buffer, 0x00);
            writeD(buffer, 0x00);
            PaperDoll doll = l2Char.getPaperDoll();
            List<L2GearItem> wornItems = Arrays.asList(doll.getUnderwear(),
                                                       doll.getRightEar(),
                                                       doll.getLeftEarring(),
                                                       doll.getNecklace(),
                                                       doll.getRightRing(),
                                                       doll.getLeftRing(),
                                                       doll.getHead(),
                                                       doll.getRightHand(),
                                                       doll.getLeftHand(),
                                                       doll.getGloves(),
                                                       doll.getChest(),
                                                       doll.getLegs(),
                                                       doll.getBoots(),
                                                       doll.getCloak(),
                                                       doll.getRightHandAdditional(),
                                                       doll.getHairAccessory(),
                                                       doll.getHairAccessory2(),
                                                       doll.getRightBracelet(),
                                                       doll.getLeftBracelet(),
                                                       doll.getDecoration1(),
                                                       doll.getDecoration2(),
                                                       doll.getDecoration3(),
                                                       doll.getDecoration4(),
                                                       doll.getDecoration5(),
                                                       doll.getDecoration6(),
                                                       doll.getBelt());

            List<Integer> wornItemsItemIds = wornItems.stream()
                                                      .map(i -> i == null ? 0 : i.getItemId())
                                                      .collect(toList());
            for (Integer itemId : wornItemsItemIds) {
                writeD(buffer, itemId);
            }

            writeD(buffer, l2Char.getAppearance().getHairStyle());
            writeD(buffer, l2Char.getAppearance().getHairColor());
            writeD(buffer, l2Char.getAppearance().getFace());

            writeF(buffer, l2Char.getMaxHp()); // hp max
            writeF(buffer, l2Char.getMaxMp()); // mp max

            /*long deleteTime = l2Char.getDeleteTimer();
            int deletedays = 0;
            if (deleteTime > 0) {
                deletedays = (int) ((deleteTime - System.currentTimeMillis()) / 1000);
            }*/
            writeD(buffer, 0); // days left before
            // delete .. if != 0
            // then char is inactive
            writeD(buffer, l2Char.getClassId().getId());
            writeD(buffer, l2Char.getId() == activeId ? 0x01 : 0x00); // c3 auto-select char

            writeC(buffer, 127);
            writeH(buffer, 0x00);
            writeH(buffer, 0x00);
            // writeD(charInfoPackage.getAugmentationId());

            // writeD(charInfoPackage.getTransformId()); // Used to display Transformations
            writeD(buffer,
                   0x00); // Currently on retail when you are on character select you don't see your transformation.

            // Freya by Vistall:
            writeD(buffer, 0x00); // npdid - 16024 Tame Tiny Baby Kookaburra A9E89C
            writeD(buffer, 0x00); // level
            writeD(buffer, 0x00); // ?
            writeD(buffer, 0x00); // food? - 1200
            writeF(buffer, 0x00); // max Hp
            writeF(buffer, 0x00); // cur Hp

            writeD(buffer, 7);//l2Char.getVitalityPoints()); // H5 Vitality
        }
    }
}
