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
package com.vvygulyarniy.l2.loginserver


import com.vvygulyarniy.l2.loginserver.logic.LoginPacketsProcessor
import com.vvygulyarniy.l2.loginserver.netty.LoginServerClientPacketDecoder
import com.vvygulyarniy.l2.loginserver.netty.LoginServerPacketEncoder
import com.vvygulyarniy.l2.loginserver.netty.NettyLoginServerHandler
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import lombok.extern.slf4j.Slf4j

/**
 * @author KenM
 */
@Slf4j
class L2LoginServer private constructor() {

    init {
        Server.serverMode = Server.MODE_LOGINSERVER

        try {
            GameServerTable.instance
            val packetProcessor = LoginPacketsProcessor(GameServerTable.instance, LoginController)
            startNettyHandler(packetProcessor)
        } catch (e: Exception) {
            System.exit(1)
        }

    }

    @Throws(InterruptedException::class)
    private fun startNettyHandler(packetProcessor: LoginPacketsProcessor) {
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
                                    NettyLoginServerHandler(LoginController, packetProcessor))
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true) // (6)

            // Bind and start to accept incoming connections.
            val f = b.bind(LOGIN_SERVER_PORT).sync() // (7)

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync()
        } finally {
            workerGroup.shutdownGracefully()
            bossGroup.shutdownGracefully()
        }
    }

    companion object {
        val LOGIN_SERVER_PORT = 2106

        @JvmStatic fun main(args: Array<String>) {
            L2LoginServer()
        }
    }
}
