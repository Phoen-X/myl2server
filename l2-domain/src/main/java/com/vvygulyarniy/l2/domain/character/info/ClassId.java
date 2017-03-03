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
package com.vvygulyarniy.l2.domain.character.info;

import com.vvygulyarniy.l2.domain.character.info.stat.BasicStatSet;
import lombok.Getter;

import static com.vvygulyarniy.l2.domain.character.info.CollisionParams.collisionParams;
import static com.vvygulyarniy.l2.domain.character.info.Race.HUMAN;

/**
 * This class defines all classes (ex : human fighter, darkFighter...) that a player can chose.<br>
 * Data:
 * <ul>
 * <li>id : The Identifier of the class</li>
 * <li>isMage : True if the class is a mage class</li>
 * <li>race : The race of this class</li>
 * <li>parent : The parent ClassId or null if this class is the root</li>
 * </ul>
 *
 * @version $Revision: 1.4.4.4 $ $Date: 2005/03/27 15:29:33 $
 */
@Getter
public enum ClassId {
    fighter(0x00,
            false,
            HUMAN,
            null,
            BasicStatSet.of(88, 55, 82, 39, 39, 38),
            collisionParams(23.0, 9.0),
            collisionParams(23.5, 8.0)),
    warrior(0x01, false, HUMAN, fighter),
    gladiator(0x02, false, HUMAN, warrior),
    warlord(0x03, false, HUMAN, warrior),
    knight(0x04, false, HUMAN, fighter),
    paladin(0x05, false, HUMAN, knight),
    darkAvenger(0x06, false, HUMAN, knight),
    rogue(0x07, false, HUMAN, fighter),
    treasureHunter(0x08, false, HUMAN, rogue),
    hawkeye(0x09, false, HUMAN, rogue),

    mage(0x0a,
         true,
         HUMAN,
         null,
         BasicStatSet.of(38, 27, 41, 79, 78, 78),
         collisionParams(22.8, 7.5),
         collisionParams(22.5, 6.5)),
    wizard(0x0b, true, HUMAN, mage),
    sorceror(0x0c, true, HUMAN, wizard),
    necromancer(0x0d, true, HUMAN, wizard),
    warlock(0x0e, true, true, HUMAN, wizard),
    cleric(0x0f, true, HUMAN, mage),
    bishop(0x10, true, HUMAN, cleric),
    prophet(0x11, true, HUMAN, cleric),

    elvenFighter(0x12,
                 false,
                 Race.ELF,
                 null,
                 BasicStatSet.of(82, 61, 82, 41, 38, 37),
                 collisionParams(24, 7.5),
                 collisionParams(23, 7.5)),
    elvenKnight(0x13, false, Race.ELF, elvenFighter),
    templeKnight(0x14, false, Race.ELF, elvenKnight),
    swordSinger(0x15, false, Race.ELF, elvenKnight),
    elvenScout(0x16, false, Race.ELF, elvenFighter),
    plainsWalker(0x17, false, Race.ELF, elvenScout),
    silverRanger(0x18, false, Race.ELF, elvenScout),

    elvenMage(0x19,
              true,
              Race.ELF,
              null,
              BasicStatSet.of(36, 32, 38, 74, 84, 77),
              collisionParams(24, 7.5),
              collisionParams(23, 7.5)),
    elvenWizard(0x1a, true, Race.ELF, elvenMage),
    spellsinger(0x1b, true, Race.ELF, elvenWizard),
    elementalSummoner(0x1c, true, true, Race.ELF, elvenWizard),
    oracle(0x1d, true, Race.ELF, elvenMage),
    elder(0x1e, true, Race.ELF, oracle),

    darkFighter(0x1f,
                false,
                Race.DARK_ELF,
                null,
                BasicStatSet.of(92, 56, 77, 42, 39, 35),
                collisionParams(24, 7.5),
                collisionParams(23.5, 7)),
    palusKnight(0x20, false, Race.DARK_ELF, darkFighter),
    shillienKnight(0x21, false, Race.DARK_ELF, palusKnight),
    bladedancer(0x22, false, Race.DARK_ELF, palusKnight),
    assassin(0x23, false, Race.DARK_ELF, darkFighter),
    abyssWalker(0x24, false, Race.DARK_ELF, assassin),
    phantomRanger(0x25, false, Race.DARK_ELF, assassin),

    darkMage(0x26,
             true,
             Race.DARK_ELF,
             null,
             BasicStatSet.of(39, 30, 37, 85, 77, 73),
             collisionParams(24, 7.5),
             collisionParams(23.5, 7)),
    darkWizard(0x27, true, Race.DARK_ELF, darkMage),
    spellhowler(0x28, true, Race.DARK_ELF, darkWizard),
    phantomSummoner(0x29, true, true, Race.DARK_ELF, darkWizard),
    shillienOracle(0x2a, true, Race.DARK_ELF, darkMage),
    shillenElder(0x2b, true, Race.DARK_ELF, shillienOracle),

    orcFighter(0x2c,
               false,
               Race.ORC,
               null,
               BasicStatSet.of(88, 50, 87, 37, 38, 41),
               collisionParams(28, 11),
               collisionParams(27, 7)),
    orcRaider(0x2d, false, Race.ORC, orcFighter),
    destroyer(0x2e, false, Race.ORC, orcRaider),
    orcMonk(0x2f, false, Race.ORC, orcFighter),
    tyrant(0x30, false, Race.ORC, orcMonk),

    orcMystic(0x31,
              false,
              Race.ORC,
              null,
              BasicStatSet.of(40, 23, 43, 77, 74, 84),
              collisionParams(27.5, 7),
              collisionParams(25.5, 8)),
    orcShaman(0x32, true, Race.ORC, orcMystic),
    overlord(0x33, true, Race.ORC, orcShaman),
    warcryer(0x34, true, Race.ORC, orcShaman),

    dwarvenFighter(0x35,
                   false,
                   Race.DWARF,
                   null,
                   BasicStatSet.of(87, 53, 85, 39, 37, 40),
                   collisionParams(18, 9),
                   collisionParams(19, 5)),
    scavenger(0x36,
              false,
              Race.DWARF,
              dwarvenFighter,
              BasicStatSet.of(40, 24, 42, 82, 72, 81),
              collisionParams(18, 9),
              collisionParams(19, 5)),
    bountyHunter(0x37,
                 false,
                 Race.DWARF,
                 scavenger,
                 BasicStatSet.of(40, 24, 42, 82, 72, 81),
                 collisionParams(18, 9),
                 collisionParams(19, 5)),
    artisan(0x38,
            false,
            Race.DWARF,
            dwarvenFighter,
            BasicStatSet.of(40, 24, 42, 82, 72, 81),
            collisionParams(18, 9),
            collisionParams(19, 5)),
    warsmith(0x39,
             false,
             Race.DWARF,
             artisan,
             BasicStatSet.of(40, 24, 42, 82, 72, 81),
             collisionParams(18, 9),
             collisionParams(19, 5)),

    /*
     * Dummy Entries (id's already in decimal format) btw FU NCSoft for the amount of work you put me through to do this!! <START>
     */
    dummyEntry1(58, false, null, null, null, null, null),
    dummyEntry2(59, false, null, null, null, null, null),
    dummyEntry3(60, false, null, null, null, null, null),
    dummyEntry4(61, false, null, null, null, null, null),
    dummyEntry5(62, false, null, null, null, null, null),
    dummyEntry6(63, false, null, null, null, null, null),
    dummyEntry7(64, false, null, null, null, null, null),
    dummyEntry8(65, false, null, null, null, null, null),
    dummyEntry9(66, false, null, null, null, null, null),
    dummyEntry10(67, false, null, null, null, null, null),
    dummyEntry11(68, false, null, null, null, null, null),
    dummyEntry12(69, false, null, null, null, null, null),
    dummyEntry13(70, false, null, null, null, null, null),
    dummyEntry14(71, false, null, null, null, null, null),
    dummyEntry15(72, false, null, null, null, null, null),
    dummyEntry16(73, false, null, null, null, null, null),
    dummyEntry17(74, false, null, null, null, null, null),
    dummyEntry18(75, false, null, null, null, null, null),
    dummyEntry19(76, false, null, null, null, null, null),
    dummyEntry20(77, false, null, null, null, null, null),
    dummyEntry21(78, false, null, null, null, null, null),
    dummyEntry22(79, false, null, null, null, null, null),
    dummyEntry23(80, false, null, null, null, null, null),
    dummyEntry24(81, false, null, null, null, null, null),
    dummyEntry25(82, false, null, null, null, null, null),
    dummyEntry26(83, false, null, null, null, null, null),
    dummyEntry27(84, false, null, null, null, null, null),
    dummyEntry28(85, false, null, null, null, null, null),
    dummyEntry29(86, false, null, null, null, null, null),
    dummyEntry30(87, false, null, null, null, null, null),
    /*
     * <END> Of Dummy entries
	 */

    /*
     * Now the bad boys! new class ids :)) (3rd classes)
     */
    duelist(0x58, false, HUMAN, gladiator),
    dreadnought(0x59, false, HUMAN, warlord),
    phoenixKnight(0x5a, false, HUMAN, paladin),
    hellKnight(0x5b, false, HUMAN, darkAvenger),
    sagittarius(0x5c, false, HUMAN, hawkeye),
    adventurer(0x5d, false, HUMAN, treasureHunter),
    archmage(0x5e, true, HUMAN, sorceror),
    soultaker(0x5f, true, HUMAN, necromancer),
    arcanaLord(0x60, true, true, HUMAN, warlock),
    cardinal(0x61, true, HUMAN, bishop),
    hierophant(0x62, true, HUMAN, prophet),

    evaTemplar(0x63, false, Race.ELF, templeKnight),
    swordMuse(0x64, false, Race.ELF, swordSinger),
    windRider(0x65, false, Race.ELF, plainsWalker),
    moonlightSentinel(0x66, false, Race.ELF, silverRanger),
    mysticMuse(0x67, true, Race.ELF, spellsinger),
    elementalMaster(0x68, true, true, Race.ELF, elementalSummoner),
    evaSaint(0x69, true, Race.ELF, elder),

    shillienTemplar(0x6a, false, Race.DARK_ELF, shillienKnight),
    spectralDancer(0x6b, false, Race.DARK_ELF, bladedancer),
    ghostHunter(0x6c, false, Race.DARK_ELF, abyssWalker),
    ghostSentinel(0x6d, false, Race.DARK_ELF, phantomRanger),
    stormScreamer(0x6e, true, Race.DARK_ELF, spellhowler),
    spectralMaster(0x6f, true, true, Race.DARK_ELF, phantomSummoner),
    shillienSaint(0x70, true, Race.DARK_ELF, shillenElder),

    titan(0x71, false, Race.ORC, destroyer),
    grandKhavatari(0x72, false, Race.ORC, tyrant),
    dominator(0x73, true, Race.ORC, overlord),
    doomcryer(0x74, true, Race.ORC, warcryer),

    fortuneSeeker(0x75, false, Race.DWARF, bountyHunter),
    maestro(0x76, false, Race.DWARF, warsmith),

    dummyEntry31(0x77, false, null, null, null, null, null),
    dummyEntry32(0x78, false, null, null, null, null, null),
    dummyEntry33(0x79, false, null, null, null, null, null),
    dummyEntry34(0x7a, false, null, null, null, null, null),

    maleSoldier(0x7b,
                false,
                Race.KAMAEL,
                null,
                BasicStatSet.of(88, 57, 80, 43, 36, 37),
                collisionParams(25.2, 8),
                collisionParams(25.2, 8)),
    femaleSoldier(0x7C,
                  false,
                  Race.KAMAEL,
                  null,
                  BasicStatSet.of(40, 28, 38, 82, 78, 75),
                  collisionParams(22.6, 7),
                  collisionParams(22.6, 7)),
    trooper(0x7D, false, Race.KAMAEL, maleSoldier),
    warder(0x7E, false, Race.KAMAEL, femaleSoldier),
    berserker(0x7F, false, Race.KAMAEL, trooper),
    maleSoulbreaker(0x80, false, Race.KAMAEL, trooper),
    femaleSoulbreaker(0x81, false, Race.KAMAEL, warder),
    arbalester(0x82, false, Race.KAMAEL, warder),
    doombringer(0x83, false, Race.KAMAEL, berserker),
    maleSoulhound(0x84, false, Race.KAMAEL, maleSoulbreaker),
    femaleSoulhound(0x85, false, Race.KAMAEL, femaleSoulbreaker),
    trickster(0x86, false, Race.KAMAEL, arbalester),
    inspector(0x87, false, Race.KAMAEL, warder), // DS: yes, both male/female inspectors use skills from warder
    judicator(0x88, false, Race.KAMAEL, inspector);

    /**
     * The Identifier of the Class
     */
    private final int id;
    /**
     * True if the class is a mage class
     */
    private final boolean isMage;

    /**
     * True if the class is a summoner class
     */
    private final boolean isSummoner;

    /**
     * The Race object of the class
     */
    @Getter
    private final Race race;

    /**
     * The parent ClassId or null if this class is a root
     */
    private final ClassId parentClassId;
    private final BasicStatSet basicStatSet;
    private final CollisionParams maleCollision;
    private final CollisionParams femaleCollision;

    /**
     * Class constructor.
     *
     * @param pId     the class Id.
     * @param pIsMage {code true} if the class is mage class.
     * @param race    the race related to the class.
     * @param pParent the parent class Id.
     */
    ClassId(int pId,
            boolean pIsMage,
            Race race,
            ClassId pParent,
            BasicStatSet basicStats,
            CollisionParams maleCollision,
            CollisionParams femaleCollision) {
        this(pId, pIsMage, false, race, pParent, basicStats, maleCollision, femaleCollision);
    }

    /**
     * Class constructor.
     *
     * @param pId         the class Id.
     * @param pIsMage     {code true} if the class is mage class.
     * @param pIsSummoner {code true} if the class is summoner class.
     * @param race        the race related to the class.
     * @param pParent     the parent class Id.
     */
    ClassId(int pId,
            boolean pIsMage,
            boolean pIsSummoner,
            Race race,
            ClassId pParent,
            BasicStatSet basicStats,
            CollisionParams maleCollision,
            CollisionParams femaleCollision) {
        id = pId;
        isMage = pIsMage;
        isSummoner = pIsSummoner;
        this.race = race;
        parentClassId = pParent;
        this.basicStatSet = basicStats;
        this.maleCollision = maleCollision;
        this.femaleCollision = femaleCollision;
    }


    ClassId(int id, boolean isMage, Race race, ClassId parentClassId, BasicStatSet basicStats) {
        this(id,
             isMage,
             race,
             parentClassId,
             basicStats,
             parentClassId.getMaleCollision(),
             parentClassId.getFemaleCollision());
    }


    ClassId(int id, boolean isMage, boolean isSummoner, Race race, ClassId parentClassId) {
        this(id,
             isMage,
             isSummoner,
             race,
             parentClassId,
             parentClassId.getBasicStatSet(),
             parentClassId.getMaleCollision(),
             parentClassId.getFemaleCollision());
    }


    ClassId(int id, boolean isMage, Race race, ClassId parentClassId) {
        this(id,
             isMage,
             race,
             parentClassId,
             parentClassId.getBasicStatSet(),
             parentClassId.getMaleCollision(),
             parentClassId.getFemaleCollision());
    }

    public static ClassId getClassId(int cId) {
        try {
            return ClassId.values()[cId];
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param cid the parent ClassId to check.
     * @return {code true} if this Class is a child of the selected ClassId.
     */
    public final boolean childOf(ClassId cid) {
        if (parentClassId == null) {
            return false;
        }

        if (parentClassId == cid) {
            return true;
        }

        return parentClassId.childOf(cid);

    }

    /**
     * @param cid the parent ClassId to check.
     * @return {code true} if this Class is equal to the selected ClassId or a child of the selected ClassId.
     */
    public final boolean equalsOrChildOf(ClassId cid) {
        return (this == cid) || childOf(cid);
    }

    /**
     * @return the child level of this Class (0=root, 1=child leve 1...)
     */
    public final int level() {
        if (parentClassId == null) {
            return 0;
        }

        return 1 + parentClassId.level();
    }

    /**
     * @return its parent Class Id
     */
    public final ClassId getParentClassId() {
        return parentClassId;
    }

    public final CollisionParams getCollisionParams(CharacterAppearance.Sex sex) {
        switch (sex) {
            case MALE:
                return maleCollision;
            case FEMALE:
                return femaleCollision;
            default:
                return maleCollision;
        }
    }
}
