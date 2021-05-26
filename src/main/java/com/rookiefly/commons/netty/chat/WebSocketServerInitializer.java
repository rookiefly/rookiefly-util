package com.rookiefly.commons.netty.chat;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {
    /*websocket访问路径*/
    private static final String WEBSOCKET_PATH = "/ws";

    private ChannelGroup channelGroup;

    public WebSocketServerInitializer(ChannelGroup channelGroup) {
        this.channelGroup = channelGroup;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //用于HTTP请求的编解码
        ch.pipeline().addLast(new HttpServerCodec());
        //用于写入一个文件的内容
        ch.pipeline().addLast(new ChunkedWriteHandler());
        //用于http请求的聚合
        ch.pipeline().addLast(new HttpObjectAggregator(64 * 1024));
        //用于WebSocket应答数据压缩传输
        ch.pipeline().addLast(new WebSocketServerCompressionHandler());
        //处理http请求，对非websocket请求的处理
        ch.pipeline().addLast(new HttpRequestHandler(WEBSOCKET_PATH));
        //根据websocket规范，处理升级握手以及各种websocket数据帧
        ch.pipeline().addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH, "", true));
        //对websocket的数据进行处理,主要处理TextWebSocketFrame数据帧和握手完成事件
        ch.pipeline().addLast(new WebSocketServerHanlder(channelGroup));
    }
}