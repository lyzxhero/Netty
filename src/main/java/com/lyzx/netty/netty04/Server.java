package com.lyzx.netty.netty04;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author hero.li
 * netty编解码之Marshalling
 * 前面讲了netty解决拆包粘包的问题
 * 我们发现拆包粘包问题的解决都只是解决netty发送字符串的情况
 * 在企业及开发中很少有直接使用字符串的，一般都有定义好的消息体，这个消息体一定对应实体类
 * 如果要传送实体类那么久一定要对实体类做序列化
 * (序列化就是把文件或者内存中的数据结构转换为字节数组以便存储或在网路传输)
 * 今天就介绍一下jboss的marshalling序列化框架
 */
public class Server{

    public static void main(String[] args) throws InterruptedException {
        //开启两个线程组，一个用于接受客户端的请求   另一个用于异步的网络IO的读写
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        //Netty启动的辅助类 里面封装了客户端和服务端的链接以及如何处理选择器 selector等逻辑
        ServerBootstrap b = new ServerBootstrap();

        //传入两个线程组,设置传输块大小为1k，添加ServerHandler类型的过滤器(表示如何处理这些消息，过滤器当然要集成netty的一个接口)
        b.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024)
                .childOption(ChannelOption.SO_KEEPALIVE,Boolean.TRUE)
                .childHandler(new ChannelInitializer<SocketChannel>(){
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception{
                        ChannelHandler[] arr = {MarshallingCodeCFactory.marshallingDecoder(),
                                                MarshallingCodeCFactory.marshallingEncoder(),
                                                new ServerHandler()};
                        ch.pipeline().addLast(arr);
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
}