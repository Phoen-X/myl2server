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


import com.l2server.network.L2LoginClient;
import com.l2server.network.SelectorConfig;
import com.l2server.network.coders.loginserver.LoginServerClientPacketDecoder;
import com.l2server.network.coders.loginserver.LoginServerServerPacketEncoder;
import com.vvygulyarniy.l2.loginserver.logic.LoginPacketsProcessor;
import com.vvygulyarniy.l2.loginserver.netty.NettyLoginServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author KenM
 */
@Slf4j
public final class L2LoginServer {

    private static L2LoginServer _instance;
    private SelectorThread<L2LoginClient> _selectorThread;

    private L2LoginServer() {
        _instance = this;
        Server.serverMode = Server.MODE_LOGINSERVER;

        try {

            LoginController.load();
            GameServerTable.getInstance();

            /*startSelectorThread();*/
            LoginPacketsProcessor packetProcessor = new LoginPacketsProcessor(GameServerTable.getInstance());
            startNettyHandler(packetProcessor);
        } catch (Exception e) {
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        new L2LoginServer();
    }

    public static L2LoginServer getInstance() {
        return _instance;
    }

    private void startNettyHandler(final LoginPacketsProcessor packetProcessor) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LoginServerClientPacketDecoder(),
                                    new LoginServerServerPacketEncoder(),
                                    new NettyLoginServerHandler(LoginController.getInstance(), packetProcessor));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(2106).sync(); // (7)

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    private void startSelectorThread() throws java.io.IOException {
        final SelectorConfig sc = new SelectorConfig();
        final L2LoginPacketHandler lph = new L2LoginPacketHandler();
        final SelectorHelper sh = new SelectorHelper();
        LoginPacketsProcessor packetsProcessor = new LoginPacketsProcessor(GameServerTable.getInstance());
        _selectorThread = new SelectorThread<>(LoginController.getInstance(), packetsProcessor, sc, sh, lph, sh, sh);
        _selectorThread.start();
        log.info("Started");
    }


}
