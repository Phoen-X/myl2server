package com.vvygulyarniy.l2.gameserver.service.characters;

import com.l2server.network.L2GameClient;
import com.l2server.network.serverpackets.game.CharCreateFail;
import com.vvygulyarniy.l2.domain.character.L2Character;
import com.vvygulyarniy.l2.domain.character.info.CharacterAppearance;
import com.vvygulyarniy.l2.domain.character.info.ClassId;
import com.vvygulyarniy.l2.domain.geo.Position;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

/**
 * Created by Phoen-X on 23.02.2017.
 */
@Slf4j
public class InMemoryCharacterRepository implements CharacterRepository {
    private final List<L2Character> characters = new ArrayList<>();

    {
        L2Character newChar = new L2Character(characters.size() + 1, "asd", ClassId.elvenFighter,
                                              new CharacterAppearance(CharacterAppearance.Sex.MALE,
                                                                      (byte) 1,
                                                                      (byte) 1,
                                                                      (byte) 1),
                                              "test_character", 1);
        newChar.setMaxHp(100);
        newChar.setCurrHp(100);
        newChar.setCurrMp(199);
        newChar.setMaxMp(200);
        newChar.setPosition(new Position(108844, -173347, -1500));
        characters.add(newChar);
    }

    @Override
    public L2Character createCharacter(L2GameClient gameClient,
                                       ClassId classId,
                                       String nickName,
                                       CharacterAppearance appearance) throws CharacterCreationException {
        if ((classId == null) || (classId.getParentClassId() != null)) {
            throw new CharacterCreationException(CharCreateFail.REASON_CREATION_FAILED);
        }

        L2Character newChar = new L2Character(characters.size() + 1,
                                              gameClient.getAccountName(),
                                              classId,
                                              appearance,
                                              nickName,
                                              1);
        newChar.setMaxHp(100);
        newChar.setCurrHp(100);
        newChar.setMaxMp(199);
        newChar.setCurrMp(200);
        newChar.setPosition(new Position(30378, 43627, -2806));
        characters.add(newChar);
        log.info("Character created: {}", newChar);
        return newChar;
    }

    @Override
    public List<L2Character> findByAccount(String accountName) {
        log.debug("Looking for characters for account {}", accountName);
        return characters.stream().filter(c -> Objects.equals(c.getAccountName(), accountName)).collect(toList());
    }
}
