package com.rookiefly.commons.netty.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

import java.util.Iterator;

/**
 * 对websocket的文本数据帧进行处理
 */
public class WebSocketServerHanlder extends SimpleChannelInboundHandler<TextWebSocketFrame> {


    private ChannelGroup channelGroup;

    public WebSocketServerHanlder(ChannelGroup channelGroup) {
        this.channelGroup = channelGroup;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        //获取当前channel用户名
        String userName = UserService.getUser(ctx.channel().id().asLongText());
        //文本帧
        String content = msg.text();
        System.out.println("Client: " + userName + " received [ " + content + " ]");
        String toName = null;
        //判断是单聊还是群发（单聊会通过  user@ msg 这种格式进行传输文本帧）
        if (content.contains("@")) {
            String[] str = content.split("@");
            content = str[1];
            //获取单聊的用户
            toName = str[0];
        }
        if (null != toName) {
            Iterator<Channel> it = channelGroup.iterator();
            while (it.hasNext()) {
                Channel channel = it.next();
                //找到指定的用户
                if (UserService.getUser(channel.id().asLongText()).equals(toName)) {
                    //单聊
                    channel.writeAndFlush(new TextWebSocketFrame(userName + "@" + content));
                }
            }
        } else {
            channelGroup.remove(ctx.channel());
            //群发实现
            channelGroup.writeAndFlush(new TextWebSocketFrame(userName + "@" + content));
            channelGroup.add(ctx.channel());
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //检测事件，如果是握手成功事件，做点业务处理
        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            String channelId = ctx.channel().id().asLongText();
            //随机为当前channel指定一个用户名
            UserService.setUser(channelId);
            System.out.println("新的客户端连接：" + UserService.getUser(channelId));
            //通知所有已经连接的 WebSocket 客户端新的客户端已经连接上了
            channelGroup.writeAndFlush(new TextWebSocketFrame(UserService.getUser(channelId) + "加入群聊"));
            //将新的 WebSocket Channel 添加到 ChannelGroup 中
            channelGroup.add(ctx.channel());
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}