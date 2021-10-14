package com.vic.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

/**
 * @Classname ClientDemo
 * @Description TODO
 * @Date 2021/10/14 下午3:05
 * @Author shengli
 */
public class ClientDemo {

    public void init() throws IOException {
        //获取通道，绑定主机和端口
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8080));
        //切换到非阻塞模式（必须）
        socketChannel.configureBlocking(false);
        //创建buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String str = scanner.next();
            //写入buffer
            buffer.put((LocalTime.now().toString()+"-----"+str).getBytes());
            //模式切换
            buffer.flip();
            //发送数据
            socketChannel.write(buffer);
            buffer.clear();
            //关闭
            //socketChannel.close();
        }
    }

    public static void main(String[] args) throws Exception{
        new ClientDemo().init();
    }
}
