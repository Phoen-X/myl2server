package com.vvygulyarniy.l2.gameserver.network.packet;

import com.vvygulyarniy.l2.gameserver.network.L2GameClient;
import com.vvygulyarniy.l2.gameserver.network.packet.client.*;

/**
 * Created by Phoen-X on 17.02.2017.
 */
public interface L2ClientPacketProcessor {
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

    void process(MoveBackwardToLocation packet, L2GameClient client);
}
