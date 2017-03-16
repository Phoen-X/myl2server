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

import com.vvygulyarniy.l2.gameserver.world.character.L2Character;
import com.vvygulyarniy.l2.gameserver.world.character.L2Npc;
import lombok.ToString;

import java.nio.ByteBuffer;

@ToString
public abstract class AbstractNpcInfo extends L2GameServerPacket {
    protected final int _runSpd, _walkSpd;
    protected final int _swimRunSpd, _swimWalkSpd;
    protected final int _flyRunSpd, _flyWalkSpd;
    protected int _x, _y, _z, _heading;
    protected int _idTemplate;
    protected boolean _isAttackable, _isSummoned;
    protected int _mAtkSpd, _pAtkSpd;
    protected double _moveMultiplier;

    protected int _rhand, _lhand, _chest, _enchantEffect;
    protected double _collisionHeight, _collisionRadius;
    protected String _name = "";
    protected String _title = "";

    public AbstractNpcInfo(L2Character l2Char) {
        _isSummoned = false; //l2Char.isShowSummonAnimation();
        _x = l2Char.getPosition().getPoint().getX();
        _y = l2Char.getPosition().getPoint().getY();
        _z = l2Char.getPosition().getPoint().getZ();
        _heading = l2Char.getPosition().getHeading();
        _mAtkSpd = 1;//l2Char.getMAtkSpd();
        _pAtkSpd = 1;//(int) l2Char.getPAtkSpd();
        _moveMultiplier = 1; //l2Char.getMovementSpeedMultiplier();
        _runSpd = (int) Math.round(l2Char.getRunSpeed() / _moveMultiplier);
        _walkSpd = (int) Math.round(l2Char.getWalkSpeed() / _moveMultiplier);
        _swimRunSpd = (int) Math.round(l2Char.getSwimRunSpeed() / _moveMultiplier);
        _swimWalkSpd = (int) Math.round(l2Char.getSwimWalkSpeed() / _moveMultiplier);
        _flyRunSpd = 0;//l2Char.isFlying() ? _runSpd : 0;
        _flyWalkSpd = 0;//l2Char.isFlying() ? _walkSpd : 0;
    }

    /**
     * Packet for Npcs
     */
    @ToString
    public static class NpcInfo extends AbstractNpcInfo {
        private final L2Npc _npc;
        private int _clanCrest = 0;
        private int _allyCrest = 0;
        private int _allyId = 0;
        private int _clanId = 0;
        private int _displayEffect = 0;

        public NpcInfo(L2Npc npc) {
            super(npc);
            _npc = npc;
            _idTemplate = npc.getNpcId(); //npc.getTemplate().getDisplayId(); // On every subclass
            _rhand = 0; //npc.getRightHandItem(); // On every subclass
            _lhand = 0; // npc.getLeftHandItem(); // On every subclass
            _enchantEffect = 0; //npc.getEnchantEffect();
            _collisionHeight = npc.getCollisionParams().getHeight();// On every subclass
            _collisionRadius = npc.getCollisionParams().getRadius();// On every subclass
            _isAttackable = true; //npc.isAutoAttackable(attacker);
            _name = npc.getName();// On every subclass

            /*if (_npc.isInvisible()) {
                _title = "Invisible";
            } else if (Config.L2JMOD_CHAMPION_ENABLE && npc.isChampion()) {
                _title = (Config.L2JMOD_CHAMP_TITLE); // On every subclass
            } else if (npc.getTemplate().isUsingServerSideTitle()) {
                _title = npc.getTemplate().getTitle(); // On every subclass
            } else {
                _title = npc.getTitle(); // On every subclass
            }*/

            /*if (Config.SHOW_NPC_LVL && (_npc instanceof L2MonsterInstance)) {
                String t = "Lv " + npc.getLevel() + (npc.isAggressive() ? "*" : "");
                if (_title != null) {
                    t += " " + _title;
                }

                _title = t;
            }*/

            // npc crest of owning clan/ally of castle
            /*if ((npc instanceof L2NpcInstance) && npc.isInsideZone(ZoneId.TOWN) && (Config.SHOW_CREST_WITHOUT_QUEST || npc
                    .getCastle()
                    .getShowNpcCrest()) && (npc.getCastle().getOwnerId() != 0)) {
                int townId = TownManager.getTown(_x, _y, _z).getTownId();
                if ((townId != 33) && (townId != 22)) {
                    L2Clan clan = ClanTable.getInstance().getClan(npc.getCastle().getOwnerId());
                    _clanCrest = clan.getCrestId();
                    _clanId = clan.getId();
                    _allyCrest = clan.getAllyCrestId();
                    _allyId = clan.getAllyId();
                }
            }*/

            /*_displayEffect = npc.getDisplayEffect();*/
        }

        @Override
        protected void writeImpl(final ByteBuffer buffer) {
            writeC(buffer, 0x0c);
            writeD(buffer, _npc.getId());
            writeD(buffer, _idTemplate + 1000000); // npctype id
            writeD(buffer, _isAttackable ? 1 : 0);
            writeD(buffer, _x);
            writeD(buffer, _y);
            writeD(buffer, _z);
            writeD(buffer, _heading);
            writeD(buffer, 0x00);
            writeD(buffer, _mAtkSpd);
            writeD(buffer, _pAtkSpd);
            writeD(buffer, _runSpd);
            writeD(buffer, _walkSpd);
            writeD(buffer, _swimRunSpd);
            writeD(buffer, _swimWalkSpd);
            writeD(buffer, _flyRunSpd);
            writeD(buffer, _flyWalkSpd);
            writeD(buffer, _flyRunSpd);
            writeD(buffer, _flyWalkSpd);
            writeF(buffer, _moveMultiplier);
            writeF(buffer, 1); //_npc.getAttackSpeedMultiplier());
            writeF(buffer, _collisionRadius);
            writeF(buffer, _collisionHeight);
            writeD(buffer, _rhand); // right hand weapon
            writeD(buffer, _chest);
            writeD(buffer, _lhand); // left hand weapon
            writeC(buffer, 1); // name above char 1=true ... ??
            writeC(buffer, 1); //_npc.isRunning() ? 1 : 0);
            writeC(buffer, 0);//_npc.isInCombat() ? 1 : 0);
            writeC(buffer, 0); //_npc.isAlikeDead() ? 1 : 0);
            writeC(buffer,
                   _isSummoned ? 2 : 0); // invisible ?? 0=false 1=true 2=summoned (only works if model has a summon animation)
            writeD(buffer, -1); // High Five NPCString ID
            writeS(buffer, _name);
            writeD(buffer, -1); // High Five NPCString ID
            writeS(buffer, _title);
            writeD(buffer, 0x00); // Title color 0=client default
            writeD(buffer, 0x00); // pvp flag
            writeD(buffer, 0x00); // karma

            writeD(buffer,
                   0); //_npc.isInvisible() ? _npc.getAbnormalVisualEffects() | AbnormalVisualEffect.STEALTH.getMask() : _npc.getAbnormalVisualEffects());
            writeD(buffer, _clanId); // clan id
            writeD(buffer, _clanCrest); // crest id
            writeD(buffer, _allyId); // ally id
            writeD(buffer, _allyCrest); // all crest

            writeC(buffer, 0); //_npc.isInsideZone(ZoneId.WATER) ? 1 : _npc.isFlying() ? 2 : 0); // C2
            writeC(buffer, 0); //_npc.getTeam().getId());

            writeF(buffer, _collisionRadius);
            writeF(buffer, _collisionHeight);
            writeD(buffer, _enchantEffect); // C4
            writeD(buffer, 0); //_npc.isFlying() ? 1 : 0); // C6
            writeD(buffer, 0x00);
            writeD(buffer, 0); //_npc.getColorEffect()); // CT1.5 Pet form and skills, Color effect
            writeC(buffer, 0x01); //_npc.isTargetable() ? 0x01 : 0x00);
            writeC(buffer, 0x01); //_npc.isShowName() ? 0x01 : 0x00);
            writeD(buffer, 0x00);//_npc.getAbnormalVisualEffectSpecial());
            writeD(buffer, _displayEffect);
        }
    }

    /*public static class TrapInfo extends AbstractNpcInfo {
        private final L2TrapInstance _trap;

        public TrapInfo(L2TrapInstance cha, L2Character attacker) {
            super(cha);

            _trap = cha;
            _idTemplate = cha.getTemplate().getDisplayId();
            _isAttackable = cha.isAutoAttackable(attacker);
            _rhand = 0;
            _lhand = 0;
            _collisionHeight = _trap.getTemplate().getfCollisionHeight();
            _collisionRadius = _trap.getTemplate().getfCollisionRadius();
            if (cha.getTemplate().isUsingServerSideName()) {
                _name = cha.getName();
            }
            _title = cha.getOwner() != null ? cha.getOwner().getName() : "";
        }

        @Override
        protected void writeImpl() {
            writeC(0x0c);
            writeD(_trap.getObjectId());
            writeD(_idTemplate + 1000000); // npctype id
            writeD(_isAttackable ? 1 : 0);
            writeD(_x);
            writeD(_y);
            writeD(_z);
            writeD(_heading);
            writeD(0x00);
            writeD(_mAtkSpd);
            writeD(_pAtkSpd);
            writeD(_runSpd);
            writeD(_walkSpd);
            writeD(_swimRunSpd);
            writeD(_swimWalkSpd);
            writeD(_flyRunSpd);
            writeD(_flyWalkSpd);
            writeD(_flyRunSpd);
            writeD(_flyWalkSpd);
            writeF(_moveMultiplier);
            writeF(_trap.getAttackSpeedMultiplier());
            writeF(_collisionRadius);
            writeF(_collisionHeight);
            writeD(_rhand); // right hand weapon
            writeD(_chest);
            writeD(_lhand); // left hand weapon
            writeC(1); // name above char 1=true ... ??
            writeC(1);
            writeC(_trap.isInCombat() ? 1 : 0);
            writeC(_trap.isAlikeDead() ? 1 : 0);
            writeC(_isSummoned ? 2 : 0); // invisible ?? 0=false 1=true 2=summoned (only works if model has a summon animation)
            writeD(-1); // High Five NPCString ID
            writeS(_name);
            writeD(-1); // High Five NPCString ID
            writeS(_title);
            writeD(0x00); // title color 0 = client default

            writeD(_trap.getPvpFlag());
            writeD(_trap.getKarma());

            writeD(_trap.isInvisible() ? _trap.getAbnormalVisualEffects() | AbnormalVisualEffect.STEALTH.getMask() : _trap
                    .getAbnormalVisualEffects());
            writeD(0x00); // clan id
            writeD(0x00); // crest id
            writeD(0000); // C2
            writeD(0000); // C2
            writeC(0000); // C2

            writeC(_trap.getTeam().getId());

            writeF(_collisionRadius);
            writeF(_collisionHeight);
            writeD(0x00); // C4
            writeD(0x00); // C6
            writeD(0x00);
            writeD(0);// CT1.5 Pet form and skills
            writeC(0x01);
            writeC(0x01);
            writeD(0x00);
        }
    }*/

    /**
     * Packet for summons.
     */
    /*public static class SummonInfo extends AbstractNpcInfo {
        private final L2Summon _summon;
        private final int _form;
        private final int _val;

        public SummonInfo(L2Summon cha, L2Character attacker, int val) {
            super(cha);
            _summon = cha;
            _val = val;
            _form = cha.getFormId();

            _isAttackable = cha.isAutoAttackable(attacker);
            _rhand = cha.getWeapon();
            _lhand = 0;
            _chest = cha.getArmor();
            _enchantEffect = cha.getTemplate().getWeaponEnchant();
            _name = cha.getName();
            _title = (cha.getOwner() != null) && cha.getOwner().isOnline() ? cha.getOwner().getName() : "";
            _idTemplate = cha.getTemplate().getDisplayId();
            _collisionHeight = cha.getTemplate().getfCollisionHeight();
            _collisionRadius = cha.getTemplate().getfCollisionRadius();
            setInvisible(cha.isInvisible());
        }

        @Override
        protected void writeImpl() {
            boolean gmSeeInvis = false;
            if (isInvisible()) {
                final L2PcInstance activeChar = getClient().getActiveChar();
                if ((activeChar != null) && activeChar.canOverrideCond(PcCondOverride.SEE_ALL_PLAYERS)) {
                    gmSeeInvis = true;
                }
            }

            writeC(0x0c);
            writeD(_summon.getObjectId());
            writeD(_idTemplate + 1000000); // npctype id
            writeD(_isAttackable ? 1 : 0);
            writeD(_x);
            writeD(_y);
            writeD(_z);
            writeD(_heading);
            writeD(0x00);
            writeD(_mAtkSpd);
            writeD(_pAtkSpd);
            writeD(_runSpd);
            writeD(_walkSpd);
            writeD(_swimRunSpd);
            writeD(_swimWalkSpd);
            writeD(_flyRunSpd);
            writeD(_flyWalkSpd);
            writeD(_flyRunSpd);
            writeD(_flyWalkSpd);
            writeF(_moveMultiplier);
            writeF(_summon.getAttackSpeedMultiplier());
            writeF(_collisionRadius);
            writeF(_collisionHeight);
            writeD(_rhand); // right hand weapon
            writeD(_chest);
            writeD(_lhand); // left hand weapon
            writeC(0x01); // name above char 1=true ... ??
            writeC(0x01); // always running 1=running 0=walking
            writeC(_summon.isInCombat() ? 1 : 0);
            writeC(_summon.isAlikeDead() ? 1 : 0);
            writeC(_val); // invisible ?? 0=false 1=true 2=summoned (only works if model has a summon animation)
            writeD(-1); // High Five NPCString ID
            writeS(_name);
            writeD(-1); // High Five NPCString ID
            writeS(_title);
            writeD(0x01);// Title color 0=client default

            writeD(_summon.getPvpFlag());
            writeD(_summon.getKarma());

            writeD(gmSeeInvis ? _summon.getAbnormalVisualEffects() | AbnormalVisualEffect.STEALTH.getMask() : _summon.getAbnormalVisualEffects());

            writeD(0x00); // clan id
            writeD(0x00); // crest id
            writeD(0x00); // C2
            writeD(0x00); // C2
            writeC(_summon.isInsideZone(ZoneId.WATER) ? 1 : _summon.isFlying() ? 2 : 0); // C2

            writeC(_summon.getTeam().getId());

            writeF(_collisionRadius);
            writeF(_collisionHeight);
            writeD(_enchantEffect); // C4
            writeD(0x00); // C6
            writeD(0x00);
            writeD(_form); // CT1.5 Pet form and skills
            writeC(0x01);
            writeC(0x01);
            writeD(_summon.getAbnormalVisualEffectSpecial());
        }
    }*/
}
