package com.star.base.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * 群聊客户端实现
 *
 * @Author: zzStar
 * @Date: 11-10-2020 15:01
 */
public class GroupChatClient {

    // 定义相关的属性
    private final String HOST = "127.0.0.1";
    private final int PORT = 6667;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    // 初始化
    public GroupChatClient() throws IOException {

        selector = Selector.open();
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(username + "is ok ...");
    }

    // 向服务器发送消息
    public void sendInfo(String info) {
        info = username + "had say : " + info;

        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 读取服务器端发送过来的消息
    public void readInfo() {
        try {
            int readChannels = selector.select();

            // 有可用的通道
            if (readChannels > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {

                    SelectionKey key = iterator.next();
                    if (key.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        socketChannel.read(buffer);
                        // 把读取到的缓冲区中的数据转成字符串
                        String msg = new String(buffer.array());
                        // 去空格
                        System.out.println(msg.trim());
                    }
                }
                iterator.remove();
            } else {
                // System.out.println("没有可用的通道...");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        GroupChatClient chatClient = new GroupChatClient();
        // 启动一个线程, 每隔三秒，读取从服务端发送的数据
        new Thread(() -> {
            while (true) {
                chatClient.readInfo();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // 发送数据给服务器端
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            chatClient.sendInfo(s);
        }
    }
}

