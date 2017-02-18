package com.l2server.network;

import com.l2server.network.clientpackets.*;

/**
 * Created by Phoen-X on 16.02.2017.
 */
public interface ClientPacketProcessor {
    void process(RequestServerList packet, L2LoginClient client);

    void process(ProtocolVersion packet, L2LoginClient client);

    void process(RequestAuthLogin packet, L2LoginClient client);

    void process(RequestServerLogin requestServerLogin, L2LoginClient client);

    void process(AuthGameGuard authGameGuard, L2LoginClient client);
}
