package com.vvygulyarniy.l2.domain.character.info;

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

    public static enum Sex {
        MALE, FEMALE, ETC;

        public static Sex valueOf(byte sexId) {
            return Sex.values()[sexId];
        }

        public int getId() {
            return this.ordinal();
        }
    }
}
