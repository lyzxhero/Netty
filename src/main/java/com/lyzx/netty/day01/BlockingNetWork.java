package com.lyzx.netty.day01;

import org.junit.Test;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author hero.li
 * 使用NIO写阻塞式的网络通信demo程序
 */
public class BlockingNetWork {

    /**
     * 客户端程序
     * @throws IOException
     */
    @Test
    public void client_v1() throws IOException {
        //1、首先获取网络通道  在获取本地文件的通道
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9988));
        FileChannel fChannel = FileChannel.open(Paths.get("F:\\1.pptx"), StandardOpenOption.READ);

        //2、把从本地通道读取的文件信息写到网络通道中
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 128);
        while(fChannel.read(buffer) != -1){
            buffer.flip();
            sChannel.write(buffer);
            buffer.clear();
        }

        //3、关闭通道
        fChannel.close();
        sChannel.close();
    }


    /**
     * 服务端程序
     * @throws IOException
     */
    @Test
    public void server_v1() throws IOException{
        //1、获取服务器端的通道并绑定端口
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        ssChannel.bind(new InetSocketAddress(9988));

        //2、等待(阻塞)接收用户的请求
        SocketChannel socketChannel = ssChannel.accept();

        //3、当用户请求发送过来时，获取服务器端的文件通道 ，把从用户请求的数据写入到文件通道中
        FileChannel fChannel = FileChannel.open(Paths.get("./current.pptx"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        ByteBuffer buffer = ByteBuffer.allocateDirect(128 * 1024);
        while(socketChannel.read(buffer) != -1){
            buffer.flip();
            fChannel.write(buffer);
            buffer.clear();
        }

        //4、关闭通道
        fChannel.close();
        socketChannel.close();
        ssChannel.close();
    }




    @Test
    public void client_v2() throws IOException{
        //1、首先获取网络通道  在获取本地文件的通道
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9988));
        FileChannel fChannel = FileChannel.open(Paths.get("F:\\1.pptx"), StandardOpenOption.READ);

        //2、把从本地通道读取的文件信息写到网络通道中
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 128);
        while(fChannel.read(buffer) != -1){
            buffer.flip();
            sChannel.write(buffer);
            buffer.clear();
        }

        //3、关闭网络通道中的输出流  即告诉服务器文件写完毕了
        sChannel.shutdownOutput();

        int len = 0;
        while((len = sChannel.read(buffer)) != -1){
            buffer.flip();
            byte[] by = new byte[buffer.limit()];
            buffer.get(by);
            System.out.println(new String(by,0,by.length));
            buffer.clear();
        }
        sChannel.shutdownInput();

        //3、关闭通道
        fChannel.close();
        sChannel.close();
    }




    @Test
    public void server_2() throws IOException{
        //1、获取服务器端的通道并绑定端口
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        ssChannel.bind(new InetSocketAddress(9988));

        //2、等待(阻塞)接收用户的请求
        SocketChannel socketChannel = ssChannel.accept();

        //3、当用户请求发送过来时，获取服务器端的文件通道 ，把从用户请求的数据写入到文件通道中
        FileChannel fChannel = FileChannel.open(Paths.get("./current.pptx"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        ByteBuffer buffer = ByteBuffer.allocateDirect(128 * 1024);
        while(socketChannel.read(buffer) != -1){
            buffer.flip();
            fChannel.write(buffer);
            buffer.clear();
        }
        socketChannel.shutdownInput();

        //4、写完文件后告诉客户端写入成功
        buffer.put("写入成功".getBytes("UTF-8"));
        buffer.flip();
        socketChannel.write(buffer);
        socketChannel.shutdownOutput();


        //5、关闭通道
        fChannel.close();
        socketChannel.close();
        ssChannel.close();
    }



}
