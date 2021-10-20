package com.star.base.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @Author: zzStar
 * @Date: 11-08-2020 16:42
 */
public class CopyTransferFromChannel {

    public static void main(String[] args) throws IOException {

        FileInputStream fileInputStream = new FileInputStream("L:\\Java\\Netty\\src\\main\\java\\com\\star\\nio\\file.txt");
        FileChannel sourceCh = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("L:\\Java\\Netty\\src\\main\\java\\com\\star\\nio\\transfer.txt");
        FileChannel destCh = fileOutputStream.getChannel();

        destCh.transferFrom(sourceCh, 0, sourceCh.size());

        sourceCh.close();
        destCh.close();
        fileInputStream.close();
        fileOutputStream.close();
    }


}
