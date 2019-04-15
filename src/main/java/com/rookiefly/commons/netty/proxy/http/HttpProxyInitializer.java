package com.rookiefly.commons.netty.proxy.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpClientCodec;

public class HttpProxyInitializer extends ChannelInitializer {

    private Channel clientChannel;

    public HttpProxyInitializer(Channel clientChannel) {
        this.clientChannel = clientChannel;
    }

    @Override
    protected void initChannel(Channel ch) {
        ch.pipeline().addLast(new HttpClientCodec());
        ch.pipeline().addLast(new HttpProxyClientHandle(clientChannel));
    }
}