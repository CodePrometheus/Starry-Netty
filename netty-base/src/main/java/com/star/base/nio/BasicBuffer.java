package com.star.base.nio;

import java.nio.IntBuffer;

/**
 * buffer的简单说明 -> 内存块，底层是一个数组
 * NIO是面向缓冲区、块编程的，而BIO以流的方式处理数据，基于字节流和字符流进行操作
 * 每个channel都会对应一个buffer
 *
 * @Author: zzStar
 * @Date: 11-08-2020 14:25
 */
public class BasicBuffer {
    public static void main(String[] args) {

        IntBuffer intBuffer = IntBuffer.allocate(5);

        // 存放数据
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i * 2);
        }

        // buffer转换，读写切换
        intBuffer.flip();

        // 读取数据
        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }
    }
}

/*
    public Buffer flip() {
        limit = position;  // 数据不能超过position
        position = 0;
        mark = -1;
        return this;
    }
*/

