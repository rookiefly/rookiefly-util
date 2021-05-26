package com.rookiefly.commons.netty.chat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.util.CharsetUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final String INDEX = "index.html";

    private String websocketUrl;

    public HttpRequestHandler(String websocketUrl) {
        this.websocketUrl = websocketUrl;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        if (websocketUrl.equalsIgnoreCase(msg.getUri())) {
            //如果该HTTP请求指向了websocketUrl的URL,那么直接交给下一个ChannelInboundHandler进行处理
            ctx.fireChannelRead(msg.retain());
        } else {
            //生成index页面的具体内容,并送往浏览器
            ByteBuf content = loadIndexHtml();
            FullHttpResponse res = new DefaultFullHttpResponse(
                    HTTP_1_1, OK, content);

            res.headers().set(HttpHeaderNames.CONTENT_TYPE,
                    "text/html; charset=UTF-8");
            HttpUtil.setContentLength(res, content.readableBytes());
            sendHttpResponse(ctx, msg, res);
        }
    }

    public static ByteBuf loadIndexHtml() {
        InputStreamReader isr = null;
        BufferedReader raf = null;
        StringBuffer content = new StringBuffer();
        try {
            isr = new InputStreamReader(ClassLoader.getSystemResourceAsStream(INDEX));
            raf = new BufferedReader(isr);
            String s = null;
            // 读取文件内容，并将其打印
            while ((s = raf.readLine()) != null) {
                content.append(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                isr.close();
                raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Unpooled.copiedBuffer(content.toString().getBytes());
    }

    /*发送应答*/
    private static void sendHttpResponse(ChannelHandlerContext ctx,
                                         FullHttpRequest req,
                                         FullHttpResponse res) {
        // 错误的请求进行处理 （code<>200).
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(),
                    CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            HttpUtil.setContentLength(res, res.content().readableBytes());
        }

        // 发送应答.
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        //对于不是长连接或者错误的请求直接关闭连接
        if (!HttpUtil.isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }
}