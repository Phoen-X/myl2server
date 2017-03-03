package com.vvygulyarniy.l2.gameserver.network;


import com.l2server.network.GameServerPacketProcessor;
import com.l2server.network.L2GameClient;
import com.l2server.network.SessionKey;
import com.l2server.network.clientpackets.game.*;
import com.l2server.network.serverpackets.game.*;
import com.vvygulyarniy.l2.domain.character.L2Character;
import com.vvygulyarniy.l2.domain.character.info.CharacterAppearance;
import com.vvygulyarniy.l2.domain.character.info.CharacterAppearance.Sex;
import com.vvygulyarniy.l2.domain.character.profession.Profession;
import com.vvygulyarniy.l2.gameserver.service.characters.CharacterCreationException;
import com.vvygulyarniy.l2.gameserver.service.characters.CharacterRepository;
import com.vvygulyarniy.l2.gameserver.world.L2World;
import com.vvygulyarniy.l2.gameserver.world.castle.CastleRegistry;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.l2server.network.L2GameClient.GameClientState.AUTHED;
import static com.l2server.network.L2GameClient.GameClientState.IN_GAME;
import static com.vvygulyarniy.l2.domain.character.info.ClassId.getClassId;
import static com.vvygulyarniy.l2.domain.sevensigns.SevenSignsWinner.NONE;
import static java.util.Comparator.comparing;

@Slf4j
public class L2GameServerPacketProcessor implements GameServerPacketProcessor {
    private final CharacterRepository characterRepository;
    private final CastleRegistry castleRegistry;
    private final L2World world;

    public L2GameServerPacketProcessor(CharacterRepository characterRepository,
                                       CastleRegistry castleRegistry,
                                       L2World world) {
        this.characterRepository = characterRepository;
        this.castleRegistry = castleRegistry;
        this.world = world;
    }

    @Override
    public void process(ProtocolVersion packet, L2GameClient client) {
        // this packet is never encrypted
        if (packet.getVersion() == -2) {
            // this is just a ping attempt from the new C2 client
            client.close((L2GameServerPacket) null);
        } else {
            log.info("Client {} protocol version: {}", client, packet.getVersion());
            client.send(new KeyPacket(client.enableCrypt(), 1));

        }
    }

    @Override
    public void process(AuthLogin packet, L2GameClient client) {
        SessionKey key = new SessionKey(packet.get_loginKey1(), packet.get_loginKey2(), packet.get_playKey1(), packet.get_playKey2());
        client.setSessionId(key);
        client.setState(AUTHED);
        client.setAccountName(packet.getLoginName());
        client.setAccountCharacters(characterRepository.findByAccount(client.getAccountName()));
        CharSelectionInfo charList = buildCharSelectionInfo(client);
        client.send(charList);
    }

    @Override
    public void process(NewCharacter packet, L2GameClient client) {
        NewCharacterSuccess responsePacket = new NewCharacterSuccess(Arrays.asList(Profession.values()));
        client.send(responsePacket);
    }

    @Override
    public void process(CharacterCreate packet, L2GameClient client) {
        CharacterAppearance appearance = new CharacterAppearance(Sex.valueOf(packet.getSex()), packet.getHairStyle(), packet.getHairColor(), packet.getFace());
        try {
            L2Character character = characterRepository.createCharacter(client,
                                                                        getClassId(packet.getClassId()),
                                                                        packet.getName(),
                                                                        appearance);
            client.addCharacter(character);
            client.send(new CharCreateOk());
        } catch (CharacterCreationException e) {
            client.send(new CharCreateFail(e.getReasonId()));
        }

    }

    @Override
    public void process(RequestGotoLobby packet, L2GameClient client) {
        client.send(buildCharSelectionInfo(client));
    }

    @Override
    public void process(RequestExFishRanking packet, L2GameClient client) {
        log.info("No handling for packet {}", packet);
    }

    @Override
    public void process(RequestPledgeSetAcademyMaster packet, L2GameClient client) {
        log.info("No handling for {}", packet);
    }

    @Override
    public void process(final CharacterSelect packet, L2GameClient client) {
        client.selectCharacter(packet.getSlotId());
        client.send(new SSQInfo(NONE));
        client.setState(IN_GAME);
        client.send(new CharSelected(client.getActiveCharacter(), client.getSessionId().playOkID1));
    }

    @Override
    public void process(EnterWorld enterWorld, L2GameClient client) {
        L2Character activeCharacter = client.getActiveCharacter();
        if (activeCharacter == null) {
            client.closeNow();
        }

        world.addCharacter(activeCharacter);

        client.send(new UserInfo(activeCharacter));
        client.send(new ItemList(new ArrayList<>(), false));
        client.send(new ExQuestItemList());

    }

    @Override
    public void process(RequestKeyMapping requestKeyMapping, L2GameClient client) {

        /*if (Config.STORE_UI_SETTINGS) {
            activeChar.send(new ExUISetting(activeChar));
        }*/
    }

    @Override
    public void process(RequestManorList packet, L2GameClient client) {
        if (client == null) {
            return;
        }
        client.send(new ExSendManorList(castleRegistry.findAll()));
    }

    @Override
    public void process(ValidatePosition packet, L2GameClient client) {
        client.getActiveCharacter().setPosition(packet.getPosition());
        client.send(new ValidateLocation(client.getActiveCharacter().getId(), packet.getPosition()));
    }

    @Override
    public void process(RequestShowMiniMap requestShowMiniMap, L2GameClient client) {
        client.send(new ShowMiniMap(1665));
    }

    @Override
    public void process(RequestAllFortressInfo requestAllFortressInfo, L2GameClient client) {
        client.send(ExShowFortressInfo.STATIC_PACKET);
    }

    @Override
    public void process(MoveBackwardToLocation packet, L2GameClient client) {
        client.getActiveCharacter().setMoveTarget(packet.getTarget());
        client.send(new ValidateLocation(client.getActiveCharacter().getId(),
                                         client.getActiveCharacter().getPosition()));
    }

    private CharSelectionInfo buildCharSelectionInfo(L2GameClient client) {
        List<L2Character> accountChars = client.getAccountCharacters();
        int activeCharId = accountChars.stream()
                                       .sorted(comparing(L2Character::getId))
                                       .findFirst()
                                       .map(L2Character::getId)
                                       .orElse(-1);
        return new CharSelectionInfo(client.getAccountName(),
                                     client.getSessionId().playOkID1,
                                     accountChars,
                                     activeCharId);
    }
}
