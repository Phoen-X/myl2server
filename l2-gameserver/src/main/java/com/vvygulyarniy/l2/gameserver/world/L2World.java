package com.vvygulyarniy.l2.gameserver.world;

import com.vvygulyarniy.l2.domain.character.L2Character;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phoen-X on 03.03.2017.
 */
public class L2World {
    private List<L2Character> onlineCharacters = new ArrayList<>();

    public L2World() {
    }

    public void addCharacter(L2Character character) {
        onlineCharacters.add(character);
    }
}
