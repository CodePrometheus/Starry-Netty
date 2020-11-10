package com.star.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * 群聊服务端实现
 *
 * @Author: zzStar
 * @Date: 11-09-2020 21:44
 */
public class GroupChatServer {

    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    // 初始化
    public GroupChatServer() {
        try {
            // 得到选择器
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 监听
    public void listen() {
        try {
            while (true) {
                int count = selector.select();

                // 有事件处理
                if (count > 0) {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();

                        if (key.isAcceptable()) {

                            // 如果是OP_ACCEPT，有新的客户端连接
                            SocketChannel socketChannel = listenChannel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            System.out.println(socketChannel.getRemoteAddress() + "running");
                        }

                        if (key.isReadable()) {

                            readData(key);
                        }

                        // 手动从集合中移除当前的selectionKey，防止重复操作
                        iterator.remove();
                    }
                } else {
                    System.out.println("waiting...");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 读取客户端消息
    private void readData(SelectionKey key) throws IOException {
        // 取到关联的channel
        SocketChannel channel = null;

        try {
            // 得到channel
            channel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = channel.read(buffer);

            if (count > 0) {
                String msg = new String(buffer.array());
                System.out.println("from client = " + msg);

                // 向其他客户端转发消息
                sendInfoToOtherClients(msg, channel);
            }
        } catch (IOException e) {
            // 处理异常消息
            try {
                System.out.println(channel.getRemoteAddress() + "已宕");
                // 取消注册
                key.cancel();
                channel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    // 转发消息给其他客户（通道）
    private void sendInfoToOtherClients(String msg, SocketChannel self) throws IOException {
        System.out.println("消息转发中...");
        // 遍历，且排除本身self
        for (SelectionKey key : selector.keys()) {
            // 通过key拿到对应的SocketChannel
            Channel targetChannel = key.channel();

            // 排除自己
            if (targetChannel instanceof SocketChannel &&
                    targetChannel != self) {
                // 转型
                SocketChannel dest = (SocketChannel) targetChannel;
                // 将msg存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                // 将buffer数据写入通道
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}
