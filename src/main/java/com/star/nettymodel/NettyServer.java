package com.star.nettymodel;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Author: zzStar
 * @Date: 11-17-2020 19:38
 */
public class NettyServer {

    public static void main(String[] args) throws InterruptedException {
        // bossGroup只是负责处理连接请求，真正和客户端业务处理交给workerGroup处理，两个都是无线循环
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {


            // 创建服务器端的启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();

            // 使用链式编程设置
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) //使用NioSocketChannel作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) // 线程队列得到的连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) // 设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // 给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    }); // 给workerGroup对应的管道设置处理器
            System.out.println("===Server is ready===");

            // 绑定一个端口且同步
            ChannelFuture future = bootstrap.bind(6668).sync();

            // 对关闭通道进行监听
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
