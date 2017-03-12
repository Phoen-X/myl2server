package com.vvygulyarniy.l2.gameserver.network;


import com.vvygulyarniy.l2.gameserver.network.packet.server.L2GameServerPacket;

/**
 * Phoen-X on 11.03.2017.
 */
public interface GameClientConnection {
    void sendPacket(L2GameServerPacket packet);
}
