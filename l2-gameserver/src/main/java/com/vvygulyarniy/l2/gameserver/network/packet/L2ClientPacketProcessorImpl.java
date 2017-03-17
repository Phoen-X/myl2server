package com.vvygulyarniy.l2.gameserver.network.packet;


import com.l2server.network.SessionKey;
import com.vvygulyarniy.l2.domain.geo.Position;
import com.vvygulyarniy.l2.gameserver.network.L2GameClient;
import com.vvygulyarniy.l2.gameserver.network.NettyClientConnection;
import com.vvygulyarniy.l2.gameserver.network.packet.client.*;
import com.vvygulyarniy.l2.gameserver.network.packet.server.*;
import com.vvygulyarniy.l2.gameserver.service.characters.CharacterCreationException;
import com.vvygulyarniy.l2.gameserver.service.characters.CharacterRepository;
import com.vvygulyarniy.l2.gameserver.world.L2World;
import com.vvygulyarniy.l2.gameserver.world.castle.CastleRegistry;
import com.vvygulyarniy.l2.gameserver.world.character.L2Player;
import com.vvygulyarniy.l2.gameserver.world.character.info.CharacterAppearance;
import com.vvygulyarniy.l2.gameserver.world.character.info.CharacterAppearance.Sex;
import com.vvygulyarniy.l2.gameserver.world.character.profession.Profession;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

import static com.vvygulyarniy.l2.domain.sevensigns.SevenSignsWinner.NONE;
import static com.vvygulyarniy.l2.gameserver.network.L2GameClient.GameClientState.AUTHED;
import static com.vvygulyarniy.l2.gameserver.network.L2GameClient.GameClientState.IN_GAME;
import static com.vvygulyarniy.l2.gameserver.world.character.info.ClassId.getClassId;
import static java.util.Comparator.comparing;

@Slf4j
public class L2ClientPacketProcessorImpl implements L2ClientPacketProcessor {
    private final CharacterRepository characterRepository;
    private final CastleRegistry castleRegistry;
    private final L2World world;

    public L2ClientPacketProcessorImpl(CharacterRepository characterRepository,
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
        SessionKey key = new SessionKey(packet.get_loginKey1(),
                                        packet.get_loginKey2(),
                                        packet.get_playKey1(),
                                        packet.get_playKey2());
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
        CharacterAppearance appearance = new CharacterAppearance(Sex.valueOf(packet.getSex()),
                                                                 packet.getHairStyle(),
                                                                 packet.getHairColor(),
                                                                 packet.getFace());
        try {
            L2Player character = characterRepository.createCharacter(client,
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
        L2Player activeCharacter = client.getActiveCharacter();
        if (activeCharacter == null) {
            client.closeNow();
        } else {
            activeCharacter.setConnection(new NettyClientConnection(client.getNetworkContext()));
            world.enterWorld(activeCharacter);
        }
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
        L2Player l2Char = client.getActiveCharacter();
        Position validatedPosition = world.validateCharacterPosition(l2Char, packet.getPosition());
        client.send(new ValidateLocation(l2Char.getId(), validatedPosition));
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
        world.move(client.getActiveCharacter(), packet.getTarget());
        client.send(new MoveToLocation(client.getActiveCharacter(),
                                       client.getActiveCharacter().getMoveTarget()));
    }

    private CharSelectionInfo buildCharSelectionInfo(L2GameClient client) {
        List<L2Player> accountChars = client.getAccountCharacters();
        int activeCharId = accountChars.stream()
                                       .sorted(comparing(L2Player::getId))
                                       .findFirst()
                                       .map(L2Player::getId)
                                       .orElse(-1);
        return new CharSelectionInfo(client.getAccountName(),
                                     client.getSessionId().playOkID1,
                                     accountChars,
                                     activeCharId);
    }
}
