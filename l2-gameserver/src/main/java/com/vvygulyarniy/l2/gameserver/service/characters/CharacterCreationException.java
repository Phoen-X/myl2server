package com.vvygulyarniy.l2.gameserver.service.characters;

import lombok.Getter;

/**
 * Created by Phoen-X on 23.02.2017.
 */
public class CharacterCreationException extends Exception {
    @Getter
    private int reasonId;

    public CharacterCreationException(int reasonId) {
        super("Cannot create character. Reason ID: " + reasonId);
        this.reasonId = reasonId;
    }
}
