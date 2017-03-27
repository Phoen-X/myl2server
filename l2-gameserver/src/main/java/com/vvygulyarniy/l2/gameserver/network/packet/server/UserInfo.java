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
import com.vvygulyarniy.l2.gameserver.world.character.L2Player;
import com.vvygulyarniy.l2.gameserver.world.character.gear.PaperDoll;
import com.vvygulyarniy.l2.gameserver.world.item.L2GearItem;
import lombok.ToString;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import static com.vvygulyarniy.l2.gameserver.world.character.info.stat.BasicStat.*;
import static java.util.stream.Collectors.toList;

@ToString
public final class UserInfo extends L2GameServerPacket {
    private final L2Player activeChar;
    private final int _runSpd, _walkSpd;
    private final int _swimRunSpd, _swimWalkSpd;
    private final int _flyRunSpd, _flyWalkSpd;
    private final double _moveMultiplier;
    private int _relation;
    private int _airShipHelm;

    public UserInfo(L2Player activeChar) {
        this.activeChar = activeChar;

        //int _territoryId = TerritoryWarManager.getInstance().getRegisteredTerritoryId(cha);
        _relation = 0; //_activeChar.isClanLeader() ? 0x40 : 0;
        /*if (_activeChar.getSiegeState() == 1) {
            if (_territoryId == 0) {
                _relation |= 0x180;
            } else {
                _relation |= 0x1000;
            }
        }
        if (_activeChar.getSiegeState() == 2) {
            _relation |= 0x80;
        }
        */
        // _isDisguised = TerritoryWarManager.getInstance().isDisguised(character.getObjectId());
        /*if (_activeChar.isInAirShip() && _activeChar.getAirShip().isCaptain(_activeChar)) {
            _airShipHelm = _activeChar.getAirShip().getHelmItemId();
        } else {
            _airShipHelm = 0;
        }*/

        _moveMultiplier = 1;
        _runSpd = (int) Math.round(activeChar.getRunSpeed() / _moveMultiplier);
        _walkSpd = (int) Math.round(activeChar.getWalkSpeed() / _moveMultiplier);
        _swimRunSpd = (int) Math.round(activeChar.getSwimRunSpeed() / _moveMultiplier);
        _swimWalkSpd = (int) Math.round(activeChar.getSwimWalkSpeed() / _moveMultiplier);
        _flyRunSpd = 0;
        _flyWalkSpd = 0;
    }

    @Override
    protected final void writeImpl(final ByteBuffer buffer) {
        writeC(buffer, 0x32);

        Point position = activeChar.getPosition().getPoint();
        writeD(buffer, position.getX());
        writeD(buffer, position.getY());
        writeD(buffer, position.getZ());
        writeD(buffer, 0); //vehicle_id

        writeD(buffer, activeChar.getId());
        writeS(buffer, activeChar.getName());
        writeD(buffer, activeChar.getClassId().getRace().getId());
        writeD(buffer, activeChar.getAppearance().getSex().getId());

        writeD(buffer, activeChar.getClassId().getId());

        writeD(buffer, activeChar.getLevel());
        writeQ(buffer, activeChar.getExp());
        writeF(buffer, 0); // High Five exp % (currExp / expToLvl)
        writeD(buffer, activeChar.getClassId().getBasicStatSet().get(STR));
        writeD(buffer, activeChar.getClassId().getBasicStatSet().get(DEX));
        writeD(buffer, activeChar.getClassId().getBasicStatSet().get(CON));
        writeD(buffer, activeChar.getClassId().getBasicStatSet().get(INT));
        writeD(buffer, activeChar.getClassId().getBasicStatSet().get(WIT));
        writeD(buffer, activeChar.getClassId().getBasicStatSet().get(MEN));
        writeD(buffer, (int) activeChar.getHp().getMaxValue());
        writeD(buffer, (int) activeChar.getHp().getCurrValue());
        writeD(buffer, (int) activeChar.getMp().getMaxValue());
        writeD(buffer, (int) activeChar.getMp().getCurrValue());
        writeD(buffer, activeChar.getSp());
        writeD(buffer, activeChar.getCurrLoad());
        writeD(buffer, activeChar.getMaxLoad());

        writeD(buffer, 20); //_activeChar.getActiveWeaponItem() != null ? 40 : 20); // 20 no weapon, 40 weapon equipped
        PaperDoll doll = activeChar.getPaperDoll();
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

        List<Integer> wornItemsObjectIds = wornItems.stream()
                                                    .map(i -> i == null ? 0 : i.getObjectId())
                                                    .collect(toList());
        List<Integer> wornItemsItemIds = wornItems.stream().map(i -> i == null ? 0 : i.getItemId()).collect(toList());
        List<Integer> wornItemsAugmentationIds = wornItems.stream()
                                                          .map(i -> i == null ? 0 : i.getAugmentationId())
                                                          .collect(toList());

        for (Integer objectId : wornItemsObjectIds) {
            writeD(buffer, objectId);
        }
        for (Integer itemId : wornItemsItemIds) {
            writeD(buffer, itemId);
        }
        for (Integer wornItemsaugmentationId : wornItemsAugmentationIds) {
            writeD(buffer, wornItemsaugmentationId);
        }
        /*for (int slot : getPaperdollOrder()) {
            writeD(_activeChar.getInventory().getPaperdollObjectId(slot));
        }

        for (int slot : getPaperdollOrder()) {
            writeD(_activeChar.getInventory().getPaperdollItemDisplayId(slot));
        }

        for (int slot : getPaperdollOrder()) {
            writeD(_activeChar.getInventory().getPaperdollAugmentationId(slot));
        }*/

        writeD(buffer, 0); //_activeChar.getInventory().getTalismanSlots()
        writeD(buffer, 1);//activeChar.getInventory().canEquipCloak() ? 1 : 0);
        writeD(buffer, 0); //(int) _activeChar.getPAtk(null));
        writeD(buffer, 0); //(int) _activeChar.getPAtkSpd());
        writeD(buffer, 0); //(int) _activeChar.getPDef(null));
        writeD(buffer, 0); //_activeChar.getEvasionRate(null));
        writeD(buffer, 0); //_activeChar.getAccuracy());
        writeD(buffer, 0); //_activeChar.getCriticalHit(null, null));
        writeD(buffer, 0); //(int) _activeChar.getMAtk(null, null));

        writeD(buffer, 0); //_activeChar.getMAtkSpd());
        writeD(buffer, 0); //(int) _activeChar.getPAtkSpd());

        writeD(buffer, 0); //(int) _activeChar.getMDef(null, null));

        writeD(buffer, 0); //_activeChar.getPvpFlag()); (byte)
        writeD(buffer, 0); //_activeChar.getKarma());

        writeD(buffer, _runSpd); //_runSpd);
        writeD(buffer, _walkSpd);
        writeD(buffer, _swimRunSpd);
        writeD(buffer, _swimWalkSpd);
        writeD(buffer, _flyRunSpd);
        writeD(buffer, _flyWalkSpd);
        writeD(buffer, _flyRunSpd);
        writeD(buffer, _flyWalkSpd);
        writeF(buffer, _moveMultiplier);
        writeF(buffer, 1); //_activeChar.getAttackSpeedMultiplier());

        writeF(buffer, activeChar.getCollisionParams().getRadius()); //_activeChar.getCollisionRadius());
        writeF(buffer, activeChar.getCollisionParams().getHeight()); //_activeChar.getCollisionHeight());

        writeD(buffer, activeChar.getAppearance().getHairStyle());
        writeD(buffer, activeChar.getAppearance().getHairColor());
        writeD(buffer, activeChar.getAppearance().getFace());
        writeD(buffer, 0); // isGM (builder level)

        /*String title = _activeChar.getTitle();
        if (_activeChar.isInvisible()) {
            title = "Invisible";
        }*/
        /*if (_activeChar.getPoly().isMorphed()) {
            final L2NpcTemplate polyObj = NpcData.getInstance().getTemplate(_activeChar.getPoly().getPolyId());
            if (polyObj != null) {
                title += " - " + polyObj.getName();
            }
        }*/
        writeS(buffer, "I am the God here");

        writeD(buffer, 0); // _activeChar.getClanId());
        writeD(buffer, 0); // _activeChar.getClanCrestId());
        writeD(buffer, 0); // _activeChar.getAllyId());
        writeD(buffer, 0); // _activeChar.getAllyCrestId()); // ally crest id
        // 0x40 leader rights
        // siege flags: attacker - 0x180 sword over name, defender - 0x80 shield, 0xC0 crown (|leader), 0x1C0 flag (|leader)
        writeD(buffer, _relation);
        writeC(buffer, 0); //activeChar.getMountType().ordinal()); // mount type
        writeC(buffer, 0); //activeChar.getPrivateStoreType().getId());
        writeC(buffer, 0); //activeChar.hasDwarvenCraft() ? 1 : 0);
        writeD(buffer, 0); //activeChar.getPkKills());
        writeD(buffer, 0); //activeChar.getPvpKills());

        writeH(buffer, 0); //_activeChar.getCubics().size());
        /*for (int cubicId : _activeChar.getCubics().keySet()) {
            writeH(cubicId);
        }*/

        writeC(buffer, 0);//_activeChar.isInPartyMatchRoom() ? 1 : 0);

        writeD(buffer,
               0); //_activeChar.isInvisible() ? _activeChar.getAbnormalVisualEffects() | AbnormalVisualEffect.STEALTH.getMask() : _activeChar.getAbnormalVisualEffects());
        writeC(buffer, 0); //_activeChar.isInsideZone(ZoneId.WATER) ? 1 : _activeChar.isFlyingMounted() ? 2 : 0);

        writeD(buffer, 0);//_activeChar.getClanPrivileges().getBitmask());

        writeH(buffer, 0);//_activeChar.getRecomLeft()); // c2 recommendations remaining
        writeH(buffer, 0);//_activeChar.getRecomHave()); // c2 recommendations received
        writeD(buffer, 0);//_activeChar.getMountNpcId() > 0 ? _activeChar.getMountNpcId() + 1000000 : 0);
        writeH(buffer, 60);//_activeChar.getInventoryLimit());

        writeD(buffer, activeChar.getClassId().getId());
        writeD(buffer, 0x00);//0x00); // special effects? circles around player...
        writeD(buffer, (int) activeChar.getCp().getMaxValue());
        writeD(buffer, (int) activeChar.getCp().getCurrValue());
        writeC(buffer, 0);//_activeChar.isMounted() || (_airShipHelm != 0) ? 0 : _activeChar.getEnchantEffect());

        writeC(buffer, 0);//_activeChar.getTeam().getId());

        writeD(buffer, 0);//_activeChar.getClanCrestLargeId());
        writeC(buffer, 0);//_activeChar.isNoble() ? 1 : 0); // 0x01: symbol on char menu ctrl+I
        writeC(buffer,
               0);//_activeChar.isHero() || (_activeChar.isGM() && Config.GM_HERO_AURA) ? 1 : 0); // 0x01: Hero Aura

        writeC(buffer, 0);//_activeChar.isFishing() ? 1 : 0); // Fishing Mode
        writeD(buffer, 0);//_activeChar.getFishx()); // fishing x
        writeD(buffer, 0);//_activeChar.getFishy()); // fishing y
        writeD(buffer, 0);//_activeChar.getFishz()); // fishing z
        writeD(buffer, activeChar.getAppearance().getNameColor());

        // new c5
        writeC(buffer, 0x01);//_activeChar.isRunning() ? 0x01 : 0x00); // changes the Speed display on Status Window

        writeD(buffer, 0);//_activeChar.getPledgeClass()); // changes the text above CP on Status Window
        writeD(buffer, 0);//_activeChar.getPledgeType());

        writeD(buffer, activeChar.getAppearance().getTitleColor());//_activeChar.getAppearance().getTitleColor());

        writeD(buffer,
               0);//_activeChar.isCursedWeaponEquipped() ? CursedWeaponsManager.getInstance().getLevel(_activeChar.getCursedWeaponEquippedId()) : 0);

        // T1 Starts
        writeD(buffer, 0);//_activeChar.getTransformationDisplayId());

        //byte attackAttribute = _activeChar.getAttackElement();
        writeH(buffer, -1);//attackAttribute);
        writeH(buffer, 0);//_activeChar.getAttackElementValue(attackAttribute));
        writeH(buffer, 0);//_activeChar.getDefenseElementValue(Elementals.FIRE));
        writeH(buffer, 0);//_activeChar.getDefenseElementValue(Elementals.WATER));
        writeH(buffer, 0);//_activeChar.getDefenseElementValue(Elementals.WIND));
        writeH(buffer, 0);//_activeChar.getDefenseElementValue(Elementals.EARTH));
        writeH(buffer, 0);//_activeChar.getDefenseElementValue(Elementals.HOLY));
        writeH(buffer, 0);//_activeChar.getDefenseElementValue(Elementals.DARK));

        writeD(buffer, 0);//_activeChar.getAgathionId());

        // T2 Starts
        writeD(buffer, 0);//_activeChar.getFame()); // Fame
        writeD(buffer, 1);//_activeChar.isMinimapAllowed() ? 1 : 0); // Minimap on Hellbound
        writeD(buffer, 0);//_activeChar.getVitalityPoints()); // Vitality Points
        writeD(buffer, 0);//_activeChar.getAbnormalVisualEffectSpecial());

        //TODO VVygulyarniy: dunno if below is needed. Was commented already
        // writeD(_territoryId); // CT2.3
        // writeD((_isDisguised ? 0x01: 0x00)); // CT2.3
        // writeD(_territoryId); // CT2.3
    }
}
