package com.vvygulyarniy.l2.domain.character.profession;

import com.vvygulyarniy.l2.domain.character.info.Race;
import com.vvygulyarniy.l2.domain.character.info.stat.BasicStatSet;
import lombok.Getter;

import static com.vvygulyarniy.l2.domain.character.info.Race.*;

/**
 * Created by Phoen-X on 26.02.2017.
 */
public enum Profession {
    HUMAN_FIGHTER(0x00, HUMAN, BasicStatSet.of(88, 55, 82, 39, 39, 38), 0),
    HUMAN_MYSTIC(0x0a, HUMAN, BasicStatSet.of(38, 27, 41, 79, 78, 78), 0),
    ELF_FIGHTER(0x12, ELF, BasicStatSet.of(82, 61, 82, 41, 38, 37), 0),
    ELF_MYSTIC(0x19, ELF, BasicStatSet.of(36, 32, 38, 74, 84, 77), 0),
    DARK_ELF_FIGHTER(0x1f, DARK_ELF, BasicStatSet.of(92, 56, 77, 42, 39, 35), 0),
    DARK_ELF_MYSTIC(0x26, DARK_ELF, BasicStatSet.of(39, 30, 37, 85, 77, 73), 0),
    ORC_FIGHTER(0x2c, ORC, BasicStatSet.of(88, 50, 87, 37, 38, 41), 0),
    ORC_MYSTIC(0x31, ORC, BasicStatSet.of(40, 23, 43, 77, 74, 84), 0),
    DWARF_FIGHTER(0x35, DWARF, BasicStatSet.of(87, 53, 85, 39, 37, 40), 0),
    DWARF_MYSTIC(0x75, DWARF, BasicStatSet.of(40, 24, 42, 82, 72, 81), 0),
    KAMAEL_FIGHTER(0x7b, KAMAEL, BasicStatSet.of(88, 57, 80, 43, 36, 37), 0),
    KAMAEL_MYSTIC(0x7C, KAMAEL, BasicStatSet.of(40, 28, 38, 82, 78, 75), 0);

    @Getter
    private final int id;
    @Getter
    private final Race race;
    @Getter
    private final BasicStatSet stats;
    @Getter
    private final int levelFrom;

    Profession(int id, Race race, BasicStatSet stats, int levelFrom) {
        this.id = id;
        this.race = race;
        this.stats = stats;
        this.levelFrom = levelFrom;
    }

    public static Profession byId(int id) {
        for (Profession profession : values()) {
            if (profession.getId() == id) {
                return profession;
            }
        }

        throw new IllegalArgumentException("Profession with id=" + id + " not found");
    }
}
