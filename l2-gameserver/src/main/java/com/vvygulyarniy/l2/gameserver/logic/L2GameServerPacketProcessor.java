package com.vvygulyarniy.l2.gameserver.logic;


import com.l2server.network.GameServerPacketProcessor;
import com.l2server.network.L2GameClient;
import com.l2server.network.clientpackets.ProtocolVersion;
import com.l2server.network.gameserverpackets.KeyPacket;
import com.l2server.network.serverpackets.L2GameServerPacket;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Phoen-X on 17.02.2017.
 */
@Slf4j
public class L2GameServerPacketProcessor implements GameServerPacketProcessor {
    @Override
    public void process(ProtocolVersion packet, L2GameClient client) {
        log.info("Got packet: {}", packet);
        // this packet is never encrypted
        if (packet.getVersion() == -2) {
            // this is just a ping attempt from the new C2 client
            client.close((L2GameServerPacket) null);
        } else {

            log.info("Client {} protocol version: {}", client, packet.getVersion());
            KeyPacket pk = new KeyPacket(client.enableCrypt(), 1);
            client.sendPacket(pk);
            client.setProtocolOk(true);
        }
    }
}
