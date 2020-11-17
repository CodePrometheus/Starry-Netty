package com.star.zerocopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @Author: zzStar
 * @Date: 11-17-2020 16:47
 */
public class NewIOServer {

    public static void main(String[] args) throws IOException {
        InetSocketAddress address = new InetSocketAddress(7001);

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        ServerSocket serverSocket = serverSocketChannel.socket();

        serverSocketChannel.bind(address);

        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);

        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();

            int readCount = 0;
            while (true) {
                try {
                    readCount = socketChannel.read(byteBuffer);
                } catch (Exception e) {
                    // e.printStackTrace();
                    break;
                }

                // 倒带 position=0
                byteBuffer.rewind();
            }
        }
    }

}
