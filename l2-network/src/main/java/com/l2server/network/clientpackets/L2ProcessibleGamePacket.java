package com.l2server.network.clientpackets;

import com.l2server.network.GameServerPacketProcessor;
import com.l2server.network.L2GameClient;

/**
 * Created by Phoen-X on 17.02.2017.
 */
public interface L2ProcessibleGamePacket {
    void process(GameServerPacketProcessor processor, L2GameClient client);
}
