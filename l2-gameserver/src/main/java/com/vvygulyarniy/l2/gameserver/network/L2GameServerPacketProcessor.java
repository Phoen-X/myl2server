package com.vvygulyarniy.l2.gameserver.network;


import com.l2server.network.GameServerPacketProcessor;
import com.l2server.network.L2GameClient;
import com.l2server.network.SessionKey;
import com.l2server.network.clientpackets.game.*;
import com.l2server.network.serverpackets.game.*;
import com.vvygulyarniy.l2.domain.character.L2Character;
import com.vvygulyarniy.l2.domain.character.info.CharacterAppearance;
import com.vvygulyarniy.l2.domain.character.info.CharacterAppearance.Sex;
import com.vvygulyarniy.l2.domain.character.info.ClassId;
import com.vvygulyarniy.l2.domain.character.info.L2CharTemplate;
import com.vvygulyarniy.l2.domain.character.info.Race;
import com.vvygulyarniy.l2.gameserver.service.characters.CharacterCreationException;
import com.vvygulyarniy.l2.gameserver.service.characters.CharacterRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
public class L2GameServerPacketProcessor implements GameServerPacketProcessor {

    private final CharacterRepository characterRepository;

    public L2GameServerPacketProcessor(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    @Override
    public void process(ProtocolVersion packet, L2GameClient client) {
        log.info("Got packet: {}", packet);
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
        CharSelectionInfo charList = buildCharSelectionInfo(client);
        client.sendPacket(charList);
    }

    @Override
    public void process(NewCharacter packet, L2GameClient client) {
        List<L2CharTemplate> templates = new ArrayList<>();
        templates.add(new L2CharTemplate(ClassId.fighter, Race.HUMAN, 1, 1, 1, 1, 1, 1));
        NewCharacterSuccess responsePacket = new NewCharacterSuccess(templates);
        client.sendPacket(responsePacket);
    }

    @Override
    public void process(CharacterCreate packet, L2GameClient client) {
        L2CharTemplate template = new L2CharTemplate(
                ClassId.getClassId(packet.getClassId()), Race.valueOf(packet.getRace()),
                packet.getBaseCon(), packet.getBaseStr(),
                packet.getBaseInt(), packet.getBaseDex(),
                packet.getBaseWit(), packet.getBaseMen());
        CharacterAppearance appearance = new CharacterAppearance(Sex.valueOf(packet.getSex()), packet.getHairStyle(), packet.getHairColor(), packet.getFace());
        try {
            L2Character character = characterRepository.createCharacter(client, template, packet.getName(), appearance);
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

    private L2CharData getCharacterData(L2Character ch) {

        return L2CharData.builder()
                .accessLevel(1)
                .name(ch.getNickName())
                .hairStyle(ch.getAppearance().getHairStyle())
                .hairColor(ch.getAppearance().getHairColor())
                .sex(ch.getAppearance().getSex().ordinal())
                .classId(ch.getClassId().getId())
                .level(ch.getLevel())
                .currentHp(100)
                .maxHp(100)
                .currentMp(100)
                .maxMp(200)
                .exp(0)
                .objectId(1)
                .race(ch.getClassId().get_race().ordinal())
                .build();
    }

    private CharSelectionInfo buildCharSelectionInfo(L2GameClient client) {
        List<L2Character> accountChars = characterRepository.findByAccount(client.getAccountName());
        List<L2CharData> charData = accountChars.stream().map(this::getCharacterData).collect(toList());
        return new CharSelectionInfo(client.getAccountName(), client.getSessionId().playOkID1, charData, -1);
    }
}
