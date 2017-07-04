package com.l2server.network.login;

import com.l2server.network.serverpackets.login.L2LoginServerPacket;

/**
 * Phoen-X on 04.07.2017.
 */
public interface LoginClientConnection {
    void send(L2LoginServerPacket packet);

    void disconnect();
}
