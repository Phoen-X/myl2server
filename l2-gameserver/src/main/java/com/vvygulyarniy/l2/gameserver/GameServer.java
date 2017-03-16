package com.vvygulyarniy.l2.gameserver;

import com.vvygulyarniy.l2.gameserver.network.handler.NettyHandler;
import com.vvygulyarniy.l2.gameserver.network.packet.L2ClientPacketProcessorImpl;
import com.vvygulyarniy.l2.gameserver.network.packet.coder.L2ClientPacketDecoder;
import com.vvygulyarniy.l2.gameserver.network.packet.coder.L2ServerPacketEncoder;
import com.vvygulyarniy.l2.gameserver.service.characters.InMemoryCharacterRepository;
import com.vvygulyarniy.l2.gameserver.world.L2World;
import com.vvygulyarniy.l2.gameserver.world.castle.CastleRegistry;
import com.vvygulyarniy.l2.gameserver.world.castle.HardCodedCastleRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by Phoen-X on 16.02.2017.
 */
@Slf4j
public class GameServer {

    public static void main(String[] args) throws Exception {
        startNettyHandler();

    }

    private static void startNettyHandler() throws InterruptedException, JDOMException, IOException, URISyntaxException {
        log.info("Starting game server");
        CastleRegistry castleRegistry = new HardCodedCastleRegistry();
        InMemoryCharacterRepository characterRepository = new InMemoryCharacterRepository();
        L2World world = new L2World(10);

        L2ClientPacketProcessorImpl packetProcessor = new L2ClientPacketProcessorImpl(characterRepository,
                                                                                      castleRegistry,
                                                                                      world);
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class) // (3)
             .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ch.pipeline().addLast(new L2ClientPacketDecoder(),
                                           new L2ServerPacketEncoder(),
                                           new NettyHandler(packetProcessor));
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
            log.info("Started");
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
