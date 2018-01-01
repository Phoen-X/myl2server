package com.vvygulyarniy.l2.loginserver.config

import com.vvygulyarniy.l2.loginserver.L2LoginServer
import com.vvygulyarniy.l2.loginserver.communication.CommunicationManager
import com.vvygulyarniy.l2.loginserver.events.router.ClientPacketsRouter
import com.vvygulyarniy.l2.loginserver.netty.LoginServerClientPacketDecoder
import com.vvygulyarniy.l2.loginserver.netty.LoginServerPacketEncoder
import com.vvygulyarniy.l2.loginserver.netty.NettyLoginServerHandler
import com.vvygulyarniy.l2.loginserver.session.SessionIdFactory
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import


@Configuration
@Import(LogicConfig::class)
open class NettyConfig {

    @Bean open fun nettyServer(communicationManager: CommunicationManager,
                               sessionFactory: SessionIdFactory,
                               packetsRouter: ClientPacketsRouter): ServerBootstrap {
        val bossGroup = NioEventLoopGroup() // (1)
        val workerGroup = NioEventLoopGroup()

        try {
            val b = ServerBootstrap() // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel::class.java) // (3)
                    .childHandler(object : ChannelInitializer<SocketChannel>() { // (4)
                        @Throws(Exception::class)
                        public override fun initChannel(ch: SocketChannel) {
                            ch.pipeline().addLast(LoginServerClientPacketDecoder(),
                                                  LoginServerPacketEncoder(),
                                                  NettyLoginServerHandler(communicationManager, sessionFactory, packetsRouter))
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true) // (6)

            val future = b.bind(L2LoginServer.LOGIN_SERVER_PORT).sync()
            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            future.channel().closeFuture().sync()
            return b
        } finally {
            workerGroup.shutdownGracefully()
            bossGroup.shutdownGracefully()
        }
    }
}