package com.vvygulyarniy.l2.gameserver.service.characters;

import com.vvygulyarniy.l2.gameserver.domain.ClassId;
import com.vvygulyarniy.l2.gameserver.network.L2GameClient;
import com.vvygulyarniy.l2.gameserver.world.character.L2Player;
import com.vvygulyarniy.l2.gameserver.world.character.info.CharacterAppearance;

import java.util.List;

public interface CharacterRepository {
    L2Player createCharacter(L2GameClient gameClient,
                             ClassId classId,
                             String nickName,
                             CharacterAppearance appearance) throws CharacterCreationException;

    List<L2Player> findByAccount(String accountName);
}
