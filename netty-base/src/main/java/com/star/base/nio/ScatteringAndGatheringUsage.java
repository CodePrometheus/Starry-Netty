package com.star.base.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Scattering: 将数据写入到buffer中，可以采用buffer数组，依次写入(分散)
 * Gathering: 从buffer读取数据时，可以采用buffer数组，依次读
 *
 * @Author: zzStar
 * @Date: 11-09-2020 18:25
 */
public class ScatteringAndGatheringUsage {

    public static void main(String[] args) throws IOException {
        // 使用一个ServerSocketChannel -> SocketChannel 网络
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        // 绑定端口到socket，并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        // 创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        // 等客户端连接（telnet）
        SocketChannel socketChannel = serverSocketChannel.accept();

        // 假定最多从客户端接收8字节
        int messageLength = 8;

        // 循环的读取
        while (true) {
            int byteRead = 0;

            while (byteRead < messageLength) {
                long l = socketChannel.read(byteBuffers);
                // 维护该读取的字节数
                byteRead += 1;
                System.out.println("byteRead = " + byteRead);

                // 使用流打印，显示当前该buffer的position和limit
                for (ByteBuffer buffer : byteBuffers) {
                    String s = "position= " + buffer.position()
                            + " , limit = " + buffer.limit();
                    System.out.println(s);
                }
            }

            // 将所有的buffer反转
            Arrays.asList(byteBuffers).forEach(ByteBuffer::flip);

            // 将数据读出显示到客户端
            long byteWrite = 0;
            while (byteWrite < messageLength) {
                long l = socketChannel.write(byteBuffers);
                byteWrite += l;
            }

            // 将所有的buffer进行clear
            Arrays.asList(byteBuffers).forEach(ByteBuffer::clear);

            System.out.println("byteRead = " + byteRead + " byteWrite = " + byteWrite
                    + " messageLength = " + messageLength);
        }
    }

}
