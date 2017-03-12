package com.vvygulyarniy.l2.gameserver.network.packet.client;

import com.vvygulyarniy.l2.gameserver.network.L2GameClient;
import com.vvygulyarniy.l2.gameserver.network.packet.L2ClientPacketProcessor;

/**
 * Created by Phoen-X on 17.02.2017.
 */
public interface L2ProcessibleGamePacket {
    void process(L2ClientPacketProcessor processor, L2GameClient client);
}
