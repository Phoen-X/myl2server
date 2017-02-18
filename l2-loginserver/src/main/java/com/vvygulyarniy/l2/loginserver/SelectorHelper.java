/*
 * Copyright (C) 2004-2015 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vvygulyarniy.l2.loginserver;


import com.l2server.network.*;
import com.l2server.network.util.crypt.ScrambledKeyPair;

import java.nio.channels.SocketChannel;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @author KenM
 */
public class SelectorHelper implements IMMOExecutor<L2LoginClient>, IClientFactory<L2LoginClient>, IAcceptFilter {
    private static final Logger LOG = Logger.getLogger(LoginController.class.getName());
    private final ThreadPoolExecutor _generalPacketsThreadPool;

    public SelectorHelper() {
        _generalPacketsThreadPool = new ThreadPoolExecutor(4, 6, 15L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

    @Override
    public void execute(ReceivablePacket packet) {
        //   _generalPacketsThreadPool.execute(packet);
    }

    @Override
    public L2LoginClient create(MMOConnection<L2LoginClient> con, ScrambledKeyPair scrambledKeyPair, byte[] blowfishKey) {
        L2LoginClient client = new L2LoginClient(con, scrambledKeyPair, blowfishKey);
        client.sendPacket(new Init(client));
        return client;
    }

    @Override
    public boolean accept(SocketChannel sc) {
        return true;
    }
}
