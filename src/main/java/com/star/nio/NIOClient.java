package com.star.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * NIO服务端
 *
 * @Author: zzStar
 * @Date: 11-09-2020 20:09
 */
public class NIOClient {

    public static void main(String[] args) throws IOException {
        // 得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);

        // 连接服务器
        if (!socketChannel.connect(inetSocketAddress)) {

            while (!socketChannel.finishConnect()) {
                System.out.println("连接需要时间，客户端不会阻塞，执行其他工作");
            }
        }

        // 连接成功，就发送数据
        String s = "Hello,Netty";
        // wrap 根据字节大小调节
        ByteBuffer buffer = ByteBuffer.wrap(s.getBytes());
        // 发送数据，将buffer数据写入channel
        socketChannel.write(buffer);
        System.in.read();
    }

}
