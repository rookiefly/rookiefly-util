package com.rookiefly.commons.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleReactorProcessor {
    private static final ExecutorService service =
            Executors.newFixedThreadPool(2 * Runtime.getRuntime().availableProcessors());
    private Selector selector;

    public SingleReactorProcessor() throws IOException {
        this.selector = SelectorProvider.provider().openSelector();
        start();
    }

    public void addChannel(SocketChannel socketChannel) throws ClosedChannelException {
        socketChannel.register(this.selector, SelectionKey.OP_READ);
    }

    public void wakeup() {
        this.selector.wakeup();
    }

    public void start() {
        service.submit(() -> {
            while (true) {
                if (selector.select(500) <= 0) {
                    continue;
                }
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isReadable()) {
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        SocketChannel socketChannel = (SocketChannel) key.channel();
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
        });
    }
}