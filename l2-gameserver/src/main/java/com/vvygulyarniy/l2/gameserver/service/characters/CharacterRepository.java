package com.vvygulyarniy.l2.gameserver.service.characters;

import com.l2server.network.L2GameClient;
import com.vvygulyarniy.l2.domain.character.L2Character;
import com.vvygulyarniy.l2.domain.character.info.CharacterAppearance;
import com.vvygulyarniy.l2.domain.character.info.ClassId;

import java.util.List;

/**
 * Created by Phoen-X on 23.02.2017.
 */
public interface CharacterRepository {
    L2Character createCharacter(L2GameClient gameClient,
                                ClassId classId,
                                String nickName,
                                CharacterAppearance appearance) throws CharacterCreationException;

    List<L2Character> findByAccount(String accountName);
}
