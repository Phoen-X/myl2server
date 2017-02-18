package com.vvygulyarniy.l2.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Arrays;

/**
 * Created by Phoen-X on 18.02.2017.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        String host = "127.0.0.1";
        int port = 2106;
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ClientHandler());
                }
            });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync(); // (5)

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    private static class ClientHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

            ByteBuf buf = (ByteBuf) msg;
            buf.markReaderIndex();
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            buf.resetReaderIndex();
            int length = buf.readShort();
            System.out.println("Length:" + length + " " + bytes.length);
            System.out.println("Got message: " + Arrays.toString(bytes));
        }
    }
}
