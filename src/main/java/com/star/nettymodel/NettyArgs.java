package com.star.nettymodel;

import io.netty.util.NettyRuntime;

/**
 * @Author: zzStar
 * @Date: 11-18-2020 18:05
 */
public class NettyArgs {
    public static void main(String[] args) {
        // CPU核数
        System.out.println(NettyRuntime.availableProcessors());
    }
}
