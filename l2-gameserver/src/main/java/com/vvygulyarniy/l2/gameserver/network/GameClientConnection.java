package com.vvygulyarniy.l2.gameserver.network;


import com.vvygulyarniy.l2.gameserver.network.packet.server.L2GameServerPacket;

public interface GameClientConnection {
    void sendPacket(L2GameServerPacket packet);
}
