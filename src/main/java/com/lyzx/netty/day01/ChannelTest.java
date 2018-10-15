package com.lyzx.netty.day01;

import org.junit.Test;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 通道（Channel）：由 java.nio.channels 包定义的。
 * Channel 表示 IO 源与目标打开的连接。
 * Channel 类似于传统的“流”。只不过 Channel本身不能直接访问数据，
 * Channel 只能与Buffer 进行交互
 *
 * Java 为 Channel 接口提供的最主要实现类如下：
 *      •FileChannel：用于读取、写入、映射和操作文件的通道。
 *      •DatagramChannel：通过 UDP 读写网络中的数据通道。
 *      •SocketChannel：通过 TCP 读写网络中的数据。
 *      •ServerSocketChannel：可以监听新进来的 TCP 连接，
 *              对每一个新进来的连接都会创建一个 SocketChannel
 */
public class ChannelTest {


    /**
     *
     * 获取通道的一种方式是对支持通道的对象调用getChannel() 方法。支持通道的类如下：
     *       FileInputStream
     *       FileOutputStream
     *       RandomAccessFile
     *       DatagramSocket
     *       Socket
     *       ServerSocket
     *      获取通道的其他方式是使用 Files 类的静态方法 newByteChannel()
     *      获取字节通道。或者通过通道的静态方法 open() 打开并返回指定通道。
     *
     *  通过Channel的方式实现大文件的拷贝(直接缓冲区)
     */
    @Test
    public void test1() throws IOException{
        FileChannel inChannel = FileChannel.open(Paths.get("E:\\video\\zy.rmvb"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("E:\\video\\zy_bak2.rmvb"), StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);


        //从一端流到另一端
//        inChannel.transferTo(0,inChannel.size(),outChannel);
        outChannel.transferFrom(inChannel,0,inChannel.size());
        inChannel.close();
        outChannel.close();
    }

    /**
     * 通过内存文件映射拷贝文件
     * 原理:
     *   操作系统的虚拟内存
     *   把操作系统上的某个文件以字节数组的形式映射到内存中，即把某个文件(文件的一部分)当做字节数组
     *   在
     */
    @Test
    public void test2() throws IOException{
        FileChannel inChannel = FileChannel.open(Paths.get("1.txt"), StandardOpenOption.READ,StandardOpenOption.WRITE);
        MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_WRITE,0,9);

        byte[] arr = "我爱你".getBytes("UTF-8");
        int index = 0;

        System.out.println(buffer);
        buffer.flip();
        while(buffer.hasRemaining()){
//            byte b = buffer.get();
//            buffer.flip();
            buffer.put(arr[index++]);
//            buffer.flip();
        }
        System.out.println(buffer);


        buffer.force();
        buffer.clear();
        inChannel.close();


//        System.out.println(buffer.limit());


    }

    @Test
    public void test3() throws Exception{
        //创建一个随机读写文件对象
        java.io.RandomAccessFile raf=new java.io.RandomAccessFile("1.txt","rw");
        long totalLen=raf.length();
        System.out.println("文件总长字节是: "+totalLen);
        //打开一个文件通道
        java.nio.channels.FileChannel channel=raf.getChannel();
        //映射文件中的某一部分数据以读写模式到内存中
        java.nio.MappedByteBuffer buffer=  channel.map(FileChannel.MapMode.READ_WRITE,0,10);
        //示例修改字节
        for(int i=0;i<10;i++){
            byte src=   buffer.get(i);
            buffer.put(i,(byte)(src-31));//修改Buffer中映射的字节的值
            System.out.println("被改为大写的原始字节是:"+src);
        }
        buffer.force();//强制输出,在buffer中的改动生效到文件
        buffer.clear();
        channel.close();
        raf.close();
    }


}
