package com.rookiefly.commons.nio;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Scanner;

public class FileUtil {

    /**
     * NIO 内存映射读大文件
     *
     * @param path
     */
    public static void readFile(String path) {
        long start = System.currentTimeMillis();
        final int BUFFER_SIZE = 0x300000;// 3M的缓冲
        File file = new File(path);
        long fileLength = file.length();
        try {
            MappedByteBuffer inputBuffer = new RandomAccessFile(file, "r").getChannel().map(FileChannel.MapMode.READ_ONLY, 0, fileLength);// 读取大文件

            byte[] dst = new byte[BUFFER_SIZE];// 每次读出3M的内容

            for (int offset = 0; offset < fileLength; offset += BUFFER_SIZE) {
                if (fileLength - offset >= BUFFER_SIZE) {
                    for (int i = 0; i < BUFFER_SIZE; i++)
                        dst[i] = inputBuffer.get(offset + i);
                } else {
                    for (int i = 0; i < fileLength - offset; i++)
                        dst[i] = inputBuffer.get(offset + i);
                }
                // 将得到的3M内容给Scanner，Scanner解析的分隔符
                Scanner scan = new Scanner(new ByteArrayInputStream(dst)).useDelimiter(" ");
                while (scan.hasNext()) {
                    System.out.print(scan.next() + " ");
                }
                scan.close();
            }
            System.out.println();
            long end = System.currentTimeMillis();
            System.out.println("NIO 内存映射读大文件，总共耗时：" + (end - start) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String path = "/Users/rookiefly/gitrepo/node/Administrative-divisions-of-China/dist/villages.json";
        readFile(path);
    }
}
