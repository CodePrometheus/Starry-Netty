package com.star.base.zerocopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @Author: zzStar
 * @Date: 11-17-2020 16:57
 */
public class NewIOClient {

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 7001));
        String fileName = "L:\\Java\\Netty\\src\\main\\java\\com\\star\\zerocopy\\Life and Death are Wearing Me Out.txt";

        // 得到一个文件channel
        FileChannel fileChannel = new FileInputStream(fileName).getChannel();

        long startTime = System.currentTimeMillis();

        // windows下调用一次transferTo只能发送8M，大文件要分段传输文件
        long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
        System.out.println("发送总字节数: " + transferCount + " 耗时: " + (System.currentTimeMillis() - startTime));

        fileChannel.close();
    }
}
