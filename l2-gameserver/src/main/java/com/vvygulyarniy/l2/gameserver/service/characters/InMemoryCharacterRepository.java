package com.vvygulyarniy.l2.gameserver.service.characters;

import com.vvygulyarniy.l2.domain.geo.Position;
import com.vvygulyarniy.l2.domain.item.L2GenericGearItem;
import com.vvygulyarniy.l2.gameserver.network.L2GameClient;
import com.vvygulyarniy.l2.gameserver.network.packet.server.CharCreateFail;
import com.vvygulyarniy.l2.gameserver.world.character.L2Player;
import com.vvygulyarniy.l2.gameserver.world.character.info.CharacterAppearance;
import com.vvygulyarniy.l2.gameserver.world.character.info.ClassId;
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
    private final List<L2Player> characters = new ArrayList<>();

    {
        L2Player newChar = new L2Player(characters.size() + 1, "asd", ClassId.elvenFighter,
                                        new CharacterAppearance(CharacterAppearance.Sex.MALE,
                                                                      (byte) 1,
                                                                      (byte) 1,
                                                                      (byte) 1),
                                        "test_character", 1);
        newChar.setMaxHp(100);
        newChar.setCurrHp(100);
        newChar.setCurrMp(199);
        newChar.setMaxMp(200);
        newChar.setPosition(new Position(15000, 119000, -11900));
        newChar.getPaperDoll().wearRightHand(new L2GenericGearItem(1, 2369, "Squire's Sword", 0));
        newChar.getPaperDoll().wearChest(new L2GenericGearItem(2, 1146, "Squire's Shirt", 0));
        characters.add(newChar);
    }

    @Override
    public L2Player createCharacter(L2GameClient gameClient,
                                    ClassId classId,
                                    String nickName,
                                    CharacterAppearance appearance) throws CharacterCreationException {
        if ((classId == null) || (classId.getParentClassId() != null)) {
            throw new CharacterCreationException(CharCreateFail.REASON_CREATION_FAILED);
        }

        L2Player newChar = new L2Player(characters.size() + 1,
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
    public List<L2Player> findByAccount(String accountName) {
        log.debug("Looking for characters for account {}", accountName);
        return characters.stream().filter(c -> Objects.equals(c.getAccountName(), accountName)).collect(toList());
    }
}
