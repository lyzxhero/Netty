package com.lyzx.netty.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.Test;

/**
 * @author hero.li
 * 第一个Netty程序
 */
public class Netty01 {

    @Test
    public void nettyServer() throws InterruptedException{
        //开启两个线程组，一个用于接受客户端的请求   另一个用于异步的网络IO的读写
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        //Netty启动的辅助类 里面封装了客户端和服务端的链接以及如何处理选择器 selector等逻辑
        ServerBootstrap b = new ServerBootstrap();

        //传入两个线程组,设置传输块大小为1k，添加ServerHandler类型的过滤器(表示如何处理这些消息，过滤器当然要集成netty的一个接口)
        b.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024)
                .childOption(ChannelOption.SO_KEEPALIVE,Boolean.FALSE)
                .childHandler(new ChannelInitializer<SocketChannel>(){
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new ServerHandler());
                    }
                });

        //同步等待绑定端口结束
        ChannelFuture f = b.bind(9988).sync();
        //等待服务端监听端口关闭
        f.channel().closeFuture().sync();
        //优雅的关闭线程组
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    @Test
    public void nettyClient() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception{
                        ch.pipeline().addLast(new ClientHandler());
                    }
                });

        ChannelFuture f = b.connect("127.0.0.1", 9988).sync();
        f.channel().closeFuture().sync();
        group.shutdownGracefully();
    }
}