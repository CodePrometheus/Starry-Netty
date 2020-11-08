package com.star.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author: zzStar
 * @Date: 11-08-2020 15:53
 */
public class NIOFileInPutChannel {
    public static void main(String[] args) throws IOException {
        String s = "Hello Netty";

        // 创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream("L:\\Java\\Netty\\src\\main\\java\\com\\star\\nio\\file.txt");
        // 通过输出流 获取对应的fileChannel，其真实类型是fileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();
        // 创建一个缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(s.getBytes());

        // 转换
        byteBuffer.flip();

        // 将buffer里的数据写入到channel中
        fileChannel.write(byteBuffer);
        fileOutputStream.close();
    }

}
