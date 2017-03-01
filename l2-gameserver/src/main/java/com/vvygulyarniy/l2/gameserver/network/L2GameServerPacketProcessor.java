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
import com.vvygulyarniy.l2.gameserver.world.castle.CastleRegistry;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

import static com.l2server.network.L2GameClient.GameClientState.AUTHED;
import static com.l2server.network.L2GameClient.GameClientState.IN_GAME;
import static com.vvygulyarniy.l2.domain.sevensigns.SevenSignsWinner.NONE;

@Slf4j
public class L2GameServerPacketProcessor implements GameServerPacketProcessor {
    private final CharacterRepository characterRepository;
    private final CastleRegistry castleRegistry;

    public L2GameServerPacketProcessor(CharacterRepository characterRepository, CastleRegistry castleRegistry) {
        this.characterRepository = characterRepository;
        this.castleRegistry = castleRegistry;
    }

    @Override
    public void process(ProtocolVersion packet, L2GameClient client) {
        // this packet is never encrypted
        if (packet.getVersion() == -2) {
            // this is just a ping attempt from the new C2 client
            client.close((L2GameServerPacket) null);
        } else {
            log.info("Client {} protocol version: {}", client, packet.getVersion());
            KeyPacket pk = new KeyPacket(client.enableCrypt(), 1);
            client.sendPacket(pk);

            client.setProtocolOk(true);
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
        client.sendPacket(charList);
    }

    @Override
    public void process(NewCharacter packet, L2GameClient client) {
        NewCharacterSuccess responsePacket = new NewCharacterSuccess(Arrays.asList(Profession.values()));
        client.sendPacket(responsePacket);
    }

    @Override
    public void process(CharacterCreate packet, L2GameClient client) {
        CharacterAppearance appearance = new CharacterAppearance(Sex.valueOf(packet.getSex()), packet.getHairStyle(), packet.getHairColor(), packet.getFace());
        try {
            L2Character character = characterRepository.createCharacter(client, Profession.byId(packet.getClassId()), packet.getName(), appearance);
            client.addCharacter(character);
            client.sendPacket(new CharCreateOk());
        } catch (CharacterCreationException e) {
            client.sendPacket(new CharCreateFail(e.getReasonId()));
        }

    }

    @Override
    public void process(RequestGotoLobby packet, L2GameClient client) {
        client.sendPacket(buildCharSelectionInfo(client));
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
        client.sendPacket(new SSQInfo(NONE));
        client.setState(IN_GAME);
        client.sendPacket(new CharSelected(getCharacterData(client.getActiveCharacter()), client.getSessionId().playOkID1));
    }

    @Override
    public void process(EnterWorld enterWorld, L2GameClient client) {
        L2Character activeCharacter = client.getActiveCharacter();
        if (activeCharacter == null) {
            client.closeNow();
        }

        client.sendPacket(new UserInfo(activeCharacter));
        client.sendPacket(new ItemList(getCharacterData(activeCharacter), false));
        //client.sendPacket(new ExQuestItemList());
    }

    @Override
    public void process(RequestKeyMapping requestKeyMapping, L2GameClient client) {

        /*if (Config.STORE_UI_SETTINGS) {
            activeChar.sendPacket(new ExUISetting(activeChar));
        }*/
    }

    @Override
    public void process(RequestManorList packet, L2GameClient client) {
        if (client == null) {
            return;
        }
        client.sendPacket(new ExSendManorList(castleRegistry.findAll()));
    }

    @Override
    public void process(ValidatePosition validatePosition, L2GameClient client) {
        client.sendPacket(new ValidateLocation(client.getActiveCharacter().getId(), client.getActiveCharacter().getPosition()));
    }

    @Override
    public void process(RequestShowMiniMap requestShowMiniMap, L2GameClient client) {
        client.sendPacket(new ShowMiniMap(1665));
    }

    @Override
    public void process(RequestAllFortressInfo requestAllFortressInfo, L2GameClient client) {
        client.sendPacket(ExShowFortressInfo.STATIC_PACKET);
    }

    private L2CharData getCharacterData(L2Character ch) {

        return L2CharData.builder()
                .accessLevel(1)
                .name(ch.getNickName())
                .hairStyle(ch.getAppearance().getHairStyle())
                .hairColor(ch.getAppearance().getHairColor())
                .sex(ch.getAppearance().getSex().ordinal())
                .classId(ch.getProfession().getId())
                .level(ch.getLevel())
                .currentHp(100)
                .maxHp(100)
                .currentMp(100)
                .maxMp(200)
                .exp(0)
                .objectId(1)
                .race(ch.getProfession().getRace().ordinal())
                .build();
    }

    private CharSelectionInfo buildCharSelectionInfo(L2GameClient client) {
        List<L2Character> accountChars = client.getAccountCharacters();
        return new CharSelectionInfo(client.getAccountName(), client.getSessionId().playOkID1, accountChars, -1);
    }
}
