package com.vvygulyarniy.l2.gameserver;

import com.l2server.network.coders.gameserver.GameServerClientPacketDecoder;
import com.l2server.network.coders.gameserver.GameServerClientPacketEncoder;
import com.vvygulyarniy.l2.gameserver.network.L2GamePacketHandler;
import com.vvygulyarniy.l2.gameserver.network.L2GameServerPacketProcessor;
import com.vvygulyarniy.l2.gameserver.network.netty.GameServerPacketHandler;
import com.vvygulyarniy.l2.gameserver.service.characters.InMemoryCharacterRepository;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Created by Phoen-X on 16.02.2017.
 */
@Slf4j
public class GameServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        log.info("Starting game server");
        L2GamePacketHandler gamePacketHandler = new L2GamePacketHandler();

        /*GameServerSelectorThread _selectorThread = new GameServerSelectorThread(gamePacketHandler);
        _selectorThread.start();*/
        startNettyHandler();
        log.info("Started");

    }

    private static void startNettyHandler() throws InterruptedException {
        L2GameServerPacketProcessor packetProcessor = new L2GameServerPacketProcessor(new InMemoryCharacterRepository());
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new GameServerClientPacketDecoder(),
                                    new GameServerClientPacketEncoder(),
                                    new GameServerPacketHandler(packetProcessor));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(9999).sync(); // (7)

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
