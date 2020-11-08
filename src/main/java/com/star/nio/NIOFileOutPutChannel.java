package com.star.nio;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author: zzStar
 * @Date: 11-08-2020 16:10
 */
public class NIOFileOutPutChannel {
    public static void main(String[] args) throws Exception {

        File file = new File("L:\\Java\\Netty\\src\\main\\java\\com\\star\\nio\\file.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        FileChannel fileChannel = fileInputStream.getChannel();

        // 创建一个缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        // 将通道的数据读入buffer
        fileChannel.read(byteBuffer);

        // 将字节转换为String
        System.out.println(new String(byteBuffer.array()));
        fileInputStream.close();
    }
}
