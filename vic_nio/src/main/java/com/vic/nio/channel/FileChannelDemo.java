package com.vic.nio.channel;

/**
 * @Classname FileChannelDemo
 * @Description TODO
 * @Date 2021/10/13 下午2:05
 * @Author shengli
 */

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * FileChannel demo
 */
public class FileChannelDemo {

    /**
     * 读取文件内容到缓冲区
     * @throws Exception
     */
    private static void fileChannelRead() throws Exception{
        //创建FileChannel
        RandomAccessFile rFile = new RandomAccessFile("01.txt", "rw");
        FileChannel channel = rFile.getChannel();

        //创建Buffer
        ByteBuffer buffer = ByteBuffer.allocate(5);

        //读取
        int read = channel.read(buffer);
        while (read != -1) {
            System.out.println("读取了：" + read);
            buffer.flip();//模式转化
            while (buffer.hasRemaining()) {
                System.out.println((char)buffer.get());
            }
            buffer.clear();//这里必须清空buffer
            read = channel.read(buffer);
        }
        rFile.close();
        System.out.println("finish");
    }

    /**
     * 通过fileChannel写文件
     * @throws Exception
     */
    private static void fileChannelWrite() throws Exception {
        //打开fileChannel
        RandomAccessFile file = new RandomAccessFile("01.txt", "rw");
        FileChannel channel = file.getChannel();
        int bufferSize = 5;
        //创建buffer
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        //要写入的内容
        String lines = "1234f\n sdfsdfdsf sdfsd 测试1234f\n sdfsdfdsf sdfsd 测试1234f\n sdfsdfdsf sdfsd 测试1234f\n sdfsdfdsf sdfsd 测试1234f\n sdfsdfdsf sdfsd 测试";
        int i = 0;
        byte[] bytes = lines.getBytes();
        for (; i + bufferSize < bytes.length; i+=bufferSize) {
            for (int j = 0; j < bufferSize; j++) {
                //写入缓冲区
                buffer.put(bytes[i + j]);//教程里没有循环写入，缓冲区的大小总不可能和文件一样大吧，超出了就会抛异常，这里自己改成了循环读取缓冲区大小的文件内容
            }
            buffer.flip();//position->0
            //写入文件
            channel.write(buffer);//个人觉得这里没必要像教程里一样放到循环里
            buffer.clear();
        }
        if (i < bytes.length) {
            for (int j = 0; i + j < bytes.length; j++) {
                //写入缓冲区
                buffer.put(bytes[i + j]);
            }
            buffer.flip();//position->0
            //写入文件
            channel.write(buffer);
            buffer.clear();
        }
        //关闭
        channel.close();
        file.close();
    }

    /**
     * 通过transferFrom方法复制文件
     * @throws Exception
     */
    private static void transferFrom() throws Exception {
        //创建两个FileChannel
        RandomAccessFile aFile = new RandomAccessFile("01.txt", "rw");
        FileChannel aFileChannel = aFile.getChannel();
        RandomAccessFile bFile = new RandomAccessFile("02.txt", "rw");
        FileChannel bFileChannel = bFile.getChannel();

        bFileChannel.transferFrom(aFileChannel, 0, aFileChannel.size());
        aFile.close();
        bFile.close();
        System.out.println("OVER");
    }

    public static void main(String[] args) throws Exception {
        //FileChannelDemo.fileChannelRead();
        //FileChannelDemo.fileChannelWrite();
        FileChannelDemo.transferFrom();
    }
}
