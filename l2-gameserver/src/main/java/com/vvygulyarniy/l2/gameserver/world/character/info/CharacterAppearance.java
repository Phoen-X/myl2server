package com.vvygulyarniy.l2.gameserver.world.character.info;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by Phoen-X on 23.02.2017.
 */
@AllArgsConstructor
@ToString
@Getter
public class CharacterAppearance {
    private final Sex sex;
    private final byte hairStyle;
    private final byte hairColor;
    private final byte face;
    private final int nameColor = 0xFFFFFF;
    private final int titleColor = 0x00FF00;

    public enum Sex {
        MALE, FEMALE, ETC;

        public static Sex valueOf(byte sexId) {
            return Sex.values()[sexId];
        }

        public int getId() {
            return this.ordinal();
        }
    }
}
