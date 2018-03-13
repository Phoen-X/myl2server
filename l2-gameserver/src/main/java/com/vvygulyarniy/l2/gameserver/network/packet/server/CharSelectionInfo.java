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

import com.vvygulyarniy.l2.gameserver.domain.ClassId;
import lombok.ToString;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@ToString
public final class CharSelectionInfo extends L2GameServerPacket {
    private final String loginName;
    private final int sessionId;
    private final List<CharacterInfo> chars;
    private int activeId;

    public CharSelectionInfo(int sessionId, String loginName, List<CharacterInfo> chars, int activeCharId) {
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
        for (CharacterInfo l2Char : chars) {
            writeS(buffer, l2Char.getName());
            writeD(buffer, l2Char.getId());
            writeS(buffer, loginName);
            writeD(buffer, sessionId);
            writeD(buffer, 0);//l2Char.getClanId());
            writeD(buffer, 0x00); // ??
            writeD(buffer, 0);//l2Char.getAppearance().getSex().ordinal());
            writeD(buffer, l2Char.getClassId().getRace().ordinal());

            writeD(buffer, l2Char.getClassId().getId());

            writeD(buffer, 0x01); // active ??
            //Point position = l2Char.getPosition().getPoint();
            writeD(buffer, 0);//position.getX());
            writeD(buffer, 0);//position.getY());
            writeD(buffer, 0); //position.getZ());

            writeF(buffer, 100);//l2Char.getHp().getCurrValue());
            writeF(buffer, 100);//l2Char.getMp().getCurrValue());

            writeD(buffer, 0);//l2Char.getSp());
            writeQ(buffer, 0);//l2Char.getExp());
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
            /*PaperDoll doll = l2Char.getPaperDoll();
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
                                                       doll.getBelt());*/

            /*List<Integer> wornItemsItemIds = wornItems.stream()
                                                      .map(i -> i == null ? 0 : i.getItemId())
                                                      .collect(toList());*/
            List<Integer> wornItemsItemIds = IntStream.rangeClosed(1, 26)
                                                      .map(i -> 0)
                                                      .boxed()
                                                      .collect(toList());

            for (Integer itemId : wornItemsItemIds) {
                writeD(buffer, itemId);
            }

            writeD(buffer, 0);//l2Char.getAppearance().getHairStyle());
            writeD(buffer, 0);//l2Char.getAppearance().getHairColor());
            writeD(buffer, 0);//l2Char.getAppearance().getFace());

            writeF(buffer, 100);//l2Char.getHp().getMaxValue()); // hp max
            writeF(buffer, 100);//l2Char.getMp().getMaxValue()); // mp max

            writeD(buffer, 0); // days left before delete .. if != 0 then char is inactive
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

    public static class CharacterInfo {
        private final int id;
        private final int level;
        private final String name;
        private final ClassId classId;

        public CharacterInfo(int id, int level, String name, ClassId classId) {
            this.id = id;
            this.level = level;
            this.name = name;
            this.classId = classId;
        }

        public int getId() {
            return id;
        }

        public int getLevel() {
            return level;
        }

        public String getName() {
            return name;
        }

        public ClassId getClassId() {
            return classId;
        }
    }
}
