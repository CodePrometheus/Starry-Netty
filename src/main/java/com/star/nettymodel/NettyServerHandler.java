package com.star.nettymodel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/***
 * @Author: zzStar
 * @Date: 11-17-2020 19:56
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    // 读取数据实例，可以读取客户端发送的消息
    // ctx：上下文对象，含有pipeline channel address
    // msg：客户端发送的数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server ctx = " + ctx);
        // 将msg转成ByteBuf，netty提供，非NIO的ByteBuffer
        ByteBuf byteBuffer = (ByteBuf) msg;
        System.out.println("客户端发送的消息是: " + byteBuffer.toString(CharsetUtil.UTF_8));
        System.out.println("客户端的地址: " + ctx.channel().remoteAddress());
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
