package com.rookiefly.commons.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static final int PORT = 1234;

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(PORT));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (selector.select() > 0) {
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()) {
                    ServerSocketChannel acceptServerSocketChannel = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = acceptServerSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    System.out.println("Accept request from " + socketChannel.getRemoteAddress());
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int len;
                    if ((len = socketChannel.read(buffer)) != -1) {
                        // 注意先调用flip方法反转Buffer,再从Buffer读取数据
                        buffer.flip();

                        // 有几种方式可以操作ByteBuffer
                        // 1.可以将当前Buffer包含的字节数组全部读取出来
                        //bytes = byteBuffer.array();
                        // System.out.print(new String(bytes));

                        // 2.类似与InputStrean的read(byte[],offset,len)方法读取
                        byte[] bytes = new byte[buffer.limit()];
                        buffer.get(bytes, 0, len);
                        System.out.println("Received message " + new String(bytes, 0, len));

                        // 3.也可以遍历Buffer读取每个字节数据
                        // 一个字节一个字节打印在控制台,但这种更慢且耗时
                        // while(byteBuffer.hasRemaining()) {
                        // System.out.print((char)byteBuffer.get());
                        // }

                        // 最后注意调用clear方法,将Buffer的位置回归到0
                        buffer.clear();

                    } else {
                        socketChannel.close();
                        key.cancel();
                        System.out.println("Received invalide data, close the connection");
                        continue;
                    }
                }
            }
        }
    }
}