package com.star.base.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 模拟NIO非阻塞网络
 * NIO客户端
 *
 * @Author: zzStar
 * @Date: 11-09-2020 19:42
 */
public class NIOServer {

    public static void main(String[] args) throws IOException {
        // 创建 ServerSocketChannel -> serverSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 得到一个Selector对象
        Selector selector = Selector.open();

        // 绑定端口
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        // 把serverSocketChannel注册到selector关心事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 循环等待客户端连接
        while (true) {
            if (selector.select(1000) == 0) {
                // 没有事件发生
                System.out.println("服务器等待了1s，无连接");
            }

            // >0 则获取相关的selectionKey集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            // 遍历
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()) {
                // 获取到selectionKey
                SelectionKey key = keyIterator.next();
                // 根据key 对应的通道发生的事件做相应的处理
                if (key.isAcceptable()) {

                    // 如果是OP_ACCEPT，有新的客户端连接
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功，生成了一个socketChannel " + socketChannel.hashCode());
                    socketChannel.configureBlocking(false);
                    // 将socketChannel注册到selector 同时关联一个buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                // read
                if (key.isReadable()) {

                    // 通过key，反向获取对应的channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    // 获取到该channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    channel.read(buffer);
                    System.out.println("from client " + new String(buffer.array()));
                }

                // 手动从集合中移除当前的selectionKey，防止重复操作
                keyIterator.remove();
            }
        }
    }
}
