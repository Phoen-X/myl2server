package com.l2server.network;

import com.l2server.network.clientpackets.game.*;

/**
 * Created by Phoen-X on 17.02.2017.
 */
public interface GameServerPacketProcessor {
    void process(ProtocolVersion packet, L2GameClient client);

    void process(AuthLogin packet, L2GameClient client);

    void process(NewCharacter packet, L2GameClient client);

    void process(CharacterCreate packet, L2GameClient client);

    void process(RequestGotoLobby packet, L2GameClient client);

    void process(RequestExFishRanking requestExFishRanking, L2GameClient client);

    void process(RequestPledgeSetAcademyMaster packet, L2GameClient client);

    void process(CharacterSelect packet, L2GameClient client);

    void process(EnterWorld enterWorld, L2GameClient client);

    void process(RequestKeyMapping requestKeyMapping, L2GameClient client);

    void process(RequestManorList packet, L2GameClient client);

    void process(ValidatePosition validatePosition, L2GameClient client);

    void process(RequestShowMiniMap requestShowMiniMap, L2GameClient client);

    void process(RequestAllFortressInfo requestAllFortressInfo, L2GameClient client);
}
