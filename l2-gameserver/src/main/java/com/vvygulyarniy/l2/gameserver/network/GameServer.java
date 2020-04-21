package com.vvygulyarniy.l2.gameserver.network;

import com.vvygulyarniy.l2.gameserver.config.SpringConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class GameServer {

    public static void main(String[] args) throws Exception {
        startNettyHandler();

    }

    private static void startNettyHandler() throws InterruptedException {
        log.info("Starting game server");
        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);

        ServerBootstrap nettyServer = ctx.getBean(ServerBootstrap.class);

        // Bind and start to accept incoming connections.
        int port = 9999;
        ChannelFuture f = nettyServer.bind(port).sync(); // (7)
        log.info("Started on port {}", port);
        // Wait until the server socket is closed.
        // In this example, this does not happen, but you can do that to gracefully
        // shut down your server.
        f.channel().closeFuture().sync();
    }
}