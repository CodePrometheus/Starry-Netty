package com.star.base.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 可以让文件直接在内存（堆外内存）修改，操作系统不需要拷贝一次
 *
 * @Author: zzStar
 * @Date: 11-08-2020 16:54
 */
public class MappedByBuffer {

    public static void main(String[] args) throws IOException {

        RandomAccessFile randomAccessFile = new RandomAccessFile("file.txt", "rw");
        // 获取对应的文件通道
        FileChannel channel = randomAccessFile.getChannel();
        // 参数1：使用的模式  参数2：可以直接修改的起始位置 参数3：映射到内存的大小，即将文件的多少个字节映射到内存
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        mappedByteBuffer.put(0, (byte) 'G');
        mappedByteBuffer.put(4, (byte) '!');
        randomAccessFile.close();
        System.out.println(" 修改成功 ");
    }
}
