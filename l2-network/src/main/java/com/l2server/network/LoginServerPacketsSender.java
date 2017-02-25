package com.l2server.network;

import com.l2server.network.serverpackets.login.L2LoginServerPacket;

/**
 * Created by Phoen-X on 19.02.2017.
 */
public interface LoginServerPacketsSender {
    void sendPacket(L2LoginServerPacket packet);
}
