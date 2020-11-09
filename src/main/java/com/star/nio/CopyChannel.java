package com.star.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 实现文件的拷贝
 *
 * @Author: zzStar
 * @Date: 11-08-2020 16:19
 */
public class CopyChannel {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("L:\\Java\\Netty\\src\\main\\java\\com\\star\\nio\\file.txt");
        FileChannel fileChannel1 = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("L:\\Java\\Netty\\src\\main\\java\\com\\star\\nio\\test.txt");
        FileChannel fileChannel2 = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        // 循环读取
        while (true) {
            // 注意清空，没有会循环得到0
            byteBuffer.clear();
            int read = fileChannel1.read(byteBuffer);
            System.out.println("read = " + read);
            if (read == -1) {
                break;
            }
            byteBuffer.flip();
            fileChannel2.write(byteBuffer);
        }

        fileInputStream.close();
        fileOutputStream.close();
    }
}
