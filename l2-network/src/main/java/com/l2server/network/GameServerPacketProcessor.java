package com.l2server.network;

import com.l2server.network.clientpackets.ProtocolVersion;

/**
 * Created by Phoen-X on 17.02.2017.
 */
public interface GameServerPacketProcessor {
    void process(ProtocolVersion packet, L2GameClient client);
}
