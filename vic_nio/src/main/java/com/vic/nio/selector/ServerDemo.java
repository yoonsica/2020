package com.vic.nio.selector;

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
 * @Classname ClientDemo
 * @Description TODO
 * @Date 2021/10/14 下午3:05
 * @Author shengli
 */
public class ServerDemo {

    public void init() throws IOException {
        //获取服务端通道
        ServerSocketChannel ssc = ServerSocketChannel.open();
        //非阻塞
        ssc.configureBlocking(false);
        //绑定端口号
        ssc.bind(new InetSocketAddress(8080));
        //获取选择器
        Selector selector = Selector.open();
        //注册通道
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        //轮寻
        while (selector.select() > 0) {
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> selectionKeyIt = selectionKeys.iterator();
            while (selectionKeyIt.hasNext()) {
                SelectionKey next = selectionKeyIt.next();
                if (next.isAcceptable()) {
                    //获取连接
                    SocketChannel acceptChannel = ssc.accept();
                    //切换非阻塞
                    acceptChannel.configureBlocking(false);
                    //注册
                    acceptChannel.register(selector, SelectionKey.OP_READ);

                } else if (next.isReadable()) {
                    SocketChannel readChannel = (SocketChannel) next.channel();
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    //读取数据
                    int length = 0;
                    while ((length = readChannel.read(readBuffer)) > 0) {
                        readBuffer.flip();
                        System.out.println(new String(readBuffer.array(), 0, length));
                        readBuffer.clear();
                    }
                }
                selectionKeyIt.remove();//TODO 这行放在循环里还是外的区别还没搞清楚
            }
        }
    }

    public static void main(String[] args) throws Exception{
        new ServerDemo().init();
    }
}
