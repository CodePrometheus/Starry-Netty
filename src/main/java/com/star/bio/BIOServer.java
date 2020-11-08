package com.star.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * BIO（blocking I/O）适用于连接数小且固定的架构
 * 对服务器资源要求比较高
 * 每个请求都要创建一个独立的线程
 *
 * @Author: zzStar
 * @Date: 11-08-2020 13:27
 */
public class BIOServer {
    public static void main(String[] args) throws IOException {

        ExecutorService executorService = Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("server is running");

        while (true) {
            System.out.println("等待连接");
            // 监听，等待客户连接，这里会阻塞
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");

            // 创建一个线程，与之通信
            executorService.execute(() -> {
                handler(socket);
            });
        }
    }

    // handler方法，和客户端通信
    public static void handler(Socket socket) {
        try {
            System.out.println("id: " + Thread.currentThread().getId() + " name: " + Thread.currentThread().getName());
            byte[] bytes = new byte[1024];
            // 获取输入流
            InputStream inputStream = socket.getInputStream();
            // 读取数据
            while (true) {
                System.out.println("id: " + Thread.currentThread().getId() + "name: " + Thread.currentThread().getName());
                System.out.println("read...");
                // 管道没有数据会阻塞
                int read = inputStream.read(bytes);
                if (read != -1) {
                    // 字节数组转为String
                    System.out.println(new String(bytes, 0, read));
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭和client连接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
