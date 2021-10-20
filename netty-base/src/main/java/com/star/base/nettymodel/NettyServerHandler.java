package com.star.base.nettymodel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * @Author: zzStar
 * @Date: 11-17-2020 19:56
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    // 读取数据实例，可以读取客户端发送的消息
    // ctx：上下文对象，含有pipeline channel address
    // msg：客户端发送的数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        // 模拟一个耗时长的业务 —> 异步执行 -> 提交该channel对应的NIOEventLoop的taskQueue中
//        Thread.sleep(10 * 1000);
//        ctx.writeAndFlush(Unpooled.copiedBuffer("hello Client ", CharsetUtil.UTF_8));

        // 解决方案1 用户程序自定义的普通任务
        ctx.channel().eventLoop().execute(() -> {
            try {
                Thread.sleep(10 * 1000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello Client ", CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                System.out.println("发生异常->" + e.getMessage());
            }
        });

        // 解决方案2 用户定时任务 -> 提交到 scheduleTaskQueue中
        ctx.channel().eventLoop().schedule(() -> {
            try {
                Thread.sleep(10 * 1000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello Client 666 ", CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                System.out.println("发生异常->" + e.getMessage());
            }
        }, 5, TimeUnit.SECONDS);


        System.out.println("go on ...");


/*
        System.out.println("服务器读取线程信息: " + Thread.currentThread().getName());
        System.out.println("server ctx = " + ctx);
        System.out.println("channel和pipeline的关系");
        Channel channel = ctx.channel();
        // 本质上是一个双向链表，出站入站
        ChannelPipeline pipeline = ctx.pipeline();

        // 将msg转成ByteBuf，netty提供，非NIO的ByteBuffer
        ByteBuf byteBuffer = (ByteBuf) msg;
        System.out.println("客户端发送的消息是: " + byteBuffer.toString(CharsetUtil.UTF_8));
        System.out.println("客户端的地址: " + channel.remoteAddress());
*/
    }


    // 数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将数据写入缓冲并刷新，也即写到管道
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello Client", CharsetUtil.UTF_8));
    }

    // 处理异常，一般需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
