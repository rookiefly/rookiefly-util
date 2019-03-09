package com.rookiefly.commons.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory
            .getLogger(TimeClientHandler.class.getName());

    private final ByteBuf firstMessage;

    /**
     * Creates a client-side handler.
     */
    public TimeClientHandler() {
        byte[] req = "QUERY TIME ORDER".getBytes();
        firstMessage = Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);
    }

    //客户端和服务端TCP链路建立成功后调用此方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        //通过此方法将消息发送给服务端
        ctx.writeAndFlush(firstMessage);
    }

    //服务端返回应答消息调用此方法
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        System.out.println("Now is : " + body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 释放资源
        logger.warn("Unexpected exception from downstream : "
                + cause.getMessage());
        ctx.close();
    }
}