package com.vic.nio.buff;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Classname BufferDemo
 * @Description TODO
 * @Date 2021/10/14 上午9:54
 * @Author shengli
 */
public class BufferDemo {

    /**
     * 子缓冲区
     */
    public void sliceDemo() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.println("position:" + buffer.position());//i
            System.out.println("limit:"+buffer.limit());//buffer.capacity()
            buffer.put((byte) i);
        }
        System.out.println("flip---------------------");
        buffer.flip();
        System.out.println("position:" + buffer.position());//0
        System.out.println("limit:"+buffer.limit());//position
        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
            System.out.println("position:" + buffer.position());//1,2,3,4
            System.out.println("limit:"+buffer.limit());//position
        }

        //创建子缓冲区
        buffer.position(3);
        buffer.limit(7);
        ByteBuffer slice = buffer.slice();
        for (int i = 0; i < slice.capacity(); i++) {
            byte b = slice.get(i);
            b *= 10;
            slice.put(i,b);
        }
        buffer.position(0);
        buffer.limit(buffer.capacity());
        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }
    }

    /**
     * 只读缓冲区
     */
    public void readOnlyDemo() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte) i);
        }
        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();
        System.out.println(readOnlyBuffer.position());
        System.out.println(readOnlyBuffer.limit());
        for (int i = 0; i < buffer.capacity(); i++) {
            byte b = buffer.get(i);
            b *= 10;
            buffer.put(i, b);
        }
        readOnlyBuffer.flip();
        while (readOnlyBuffer.hasRemaining()) {
            System.out.println(readOnlyBuffer.get());
        }
    }

    /**
     * 直接缓冲区
     */
    public void directBufferDemo() throws Exception{
        FileInputStream fis = new FileInputStream("01.txt");
        FileChannel inChannel = fis.getChannel();

        FileOutputStream fos = new FileOutputStream("02.txt");
        FileChannel outChannel = fos.getChannel();

        //创建直接缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(3);
        while ((inChannel.read(buffer))!=-1) {
            buffer.flip();
            outChannel.write(buffer);
            buffer.clear();
        }
        System.out.println("copy finish");
    }

    /**
     * 内存映射IO
     * @throws Exception
     */
    public void mappedIO_Demo() throws Exception {
        RandomAccessFile raf = new RandomAccessFile("01.txt", "rw");
        FileChannel channel = raf.getChannel();
        MappedByteBuffer mbb = channel.map(FileChannel.MapMode.READ_WRITE, 0, 1000);
        mbb.put(0, (byte) 97);
        mbb.put(999, (byte) 122);
        raf.close();
    }

    public static void main(String[] args) throws Exception{
        BufferDemo bufferDemo = new BufferDemo();
        //bufferDemo.sliceDemo();
        //bufferDemo.readOnlyDemo();
        //bufferDemo.directBufferDemo();
        bufferDemo.mappedIO_Demo();
    }
}
