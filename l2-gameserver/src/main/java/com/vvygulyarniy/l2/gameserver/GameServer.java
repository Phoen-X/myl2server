package com.vvygulyarniy.l2.gameserver;

import com.vvygulyarniy.l2.gameserver.network.GameServerSelectorThread;
import com.vvygulyarniy.l2.gameserver.network.L2GamePacketHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Created by Phoen-X on 16.02.2017.
 */
@Slf4j
public class GameServer {

    public static void main(String[] args) throws IOException {
        log.info("Starting game server");
        L2GamePacketHandler gamePacketHandler = new L2GamePacketHandler();
        GameServerSelectorThread _selectorThread = new GameServerSelectorThread(gamePacketHandler);
        _selectorThread.start();
        log.info("Started");

    }
}
