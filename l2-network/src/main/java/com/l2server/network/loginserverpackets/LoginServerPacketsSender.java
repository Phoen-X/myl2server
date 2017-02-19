package com.l2server.network.loginserverpackets;

import com.l2server.network.L2LoginServerPacket;

/**
 * Created by Phoen-X on 19.02.2017.
 */
public interface LoginServerPacketsSender {
    void sendPacket(L2LoginServerPacket packet);
}
