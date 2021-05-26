package com.rookiefly.commons.netty.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;

public class WebSocketServer {

    private static EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private static EventLoopGroup workerGroup = new NioEventLoopGroup();
    private static ServerBootstrap bootstrap = new ServerBootstrap();

    private static final int PORT = 8761;

    //创建 DefaultChannelGroup，用来保存所有已经连接的 WebSocket Channel，群发和一对一功能可以用上
    private static ChannelGroup channelGroup =
            new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

    public static void startServer() {
        try {
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new WebSocketServerInitializer(channelGroup));
            Channel ch = bootstrap.bind(PORT).sync().channel();
            System.out.println("打开浏览器访问: http://127.0.0.1:" + PORT + '/');
            ch.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        startServer();
    }
}