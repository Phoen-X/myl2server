package com.vvygulyarniy.l2.gameserver.config;

import com.google.common.eventbus.EventBus;
import com.vvygulyarniy.l2.gameserver.network.handler.NettyHandler;
import com.vvygulyarniy.l2.gameserver.network.packet.L2ClientPacketProcessor;
import com.vvygulyarniy.l2.gameserver.network.packet.L2ClientPacketProcessorImpl;
import com.vvygulyarniy.l2.gameserver.network.packet.coder.L2ClientPacketDecoder;
import com.vvygulyarniy.l2.gameserver.network.packet.coder.L2ServerPacketEncoder;
import com.vvygulyarniy.l2.gameserver.service.characters.CharacterRepository;
import com.vvygulyarniy.l2.gameserver.service.characters.InMemoryCharacterRepository;
import com.vvygulyarniy.l2.gameserver.world.L2World;
import com.vvygulyarniy.l2.gameserver.world.castle.CastleRegistry;
import com.vvygulyarniy.l2.gameserver.world.castle.HardCodedCastleRegistry;
import com.vvygulyarniy.l2.gameserver.world.event.CharRegeneratedNotificator;
import com.vvygulyarniy.l2.gameserver.world.event.MoveStoppedEventListener;
import com.vvygulyarniy.l2.gameserver.world.management.CharRegenerationManager;
import com.vvygulyarniy.l2.gameserver.world.time.CommonClockGameTimeProvider;
import com.vvygulyarniy.l2.gameserver.world.time.GameTimeProvider;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Clock;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MILLISECONDS;


/**
 * Phoen-X on 16.03.2017.
 */
@Configuration
public class SpringConfig {

    @Bean
    public GameTimeProvider gameTimeProvider() {
        return CommonClockGameTimeProvider.withClock(Clock.systemUTC());
    }
    @Bean
    public ScheduledExecutorService scheduler() {
        return Executors.newScheduledThreadPool(4);
    }

    @Bean
    EventBus createEventBus() {
        return new EventBus("game_events");
    }

    @Bean
    public L2World world(EventBus eventBus,
                         GameTimeProvider gameTimeProvider) throws JDOMException, IOException, URISyntaxException {
        return new L2World(gameTimeProvider, eventBus, 50);
    }

    @Bean
    public CharRegenerationManager regenManager(EventBus bus, ScheduledExecutorService scheduler) {
        return new CharRegenerationManager(scheduler, bus, Clock.systemUTC(), 1000, MILLISECONDS);
    }

    @Bean
    public MoveStoppedEventListener moveStoppedListener(final EventBus bus) {
        return new MoveStoppedEventListener(bus);
    }

    /*@Bean
    public NpcSpawnedNotificator npcSpawnedNotificator(L2World world, EventBus bus) {
        return new NpcSpawnedNotificator(world, bus);
    }*/

    @Bean
    public CharRegeneratedNotificator regenerationNotificator(L2World world, EventBus bus) {
        return new CharRegeneratedNotificator(world, bus);
    }

    @Bean
    public CastleRegistry castleRegistry() {
        return new HardCodedCastleRegistry();
    }

    @Bean
    public CharacterRepository charRepo() {
        return new InMemoryCharacterRepository();
    }

    @Bean
    public L2ClientPacketProcessor packetProcessor(CharacterRepository characterRepository,
                                                   CastleRegistry castleRegistry, EventBus eventBus) {
        return new L2ClientPacketProcessorImpl(characterRepository, castleRegistry, eventBus);
    }

    @Bean(destroyMethod = "shutdownGracefully")
    public EventLoopGroup bossGroup() {
        return new NioEventLoopGroup();
    }

    @Bean(destroyMethod = "shutdownGracefully")
    public EventLoopGroup workerGroup() {
        return new NioEventLoopGroup();
    }

    @Bean
    public ServerBootstrap nettyServer(@Qualifier("bossGroup") EventLoopGroup bossGroup,
                                       @Qualifier("workerGroup") EventLoopGroup workerGroup,
                                       final L2ClientPacketProcessor packetProcessor) {
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

        return b;
    }

}
