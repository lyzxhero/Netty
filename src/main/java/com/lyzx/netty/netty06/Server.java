package com.lyzx.netty.netty06;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * @author hero.li
 * netty的通信方式
 * 1 、使用长连接通道不断开的形式进行通信，也就是服务器和客户端的通道一直处于开启状态，
 *      适用于服务器性能足够好,并且客户端数量也比较少的情况
 *
 * 2 、一次性批量提交数据，采用短连接方式。也就是我们会把数据保存在本地，
 *   当数据量达到临界值时进行一次性批量提交，又或者根据定时任务轮询提交，这种情况弊端是做不到实时性传输，
 *   适用于实时性不高的应用程序
 *
 * 3 、我们可以使用一种特殊的长连接，在指定某一时间之内，服务器与某台客户端没有任何通信，则断开连接。
 *     下次连接则是客户端向服务器发送请求的时候，再次建立连接。需要考虑2个因素：
 *          (1) 如何在超时（即服务器和客户端没有任何通信）后关闭通道？关闭通道后我们又如何再次建立连接？
 *                  netty帮我们实现了超时断开的功能，当客户端再次连接服务器时建立连接
 *          (2) 客户端宕机时，我们无需考虑，下次客户端重启之后我们就可以与服务器建立连接，但是服务器宕机时，我们的客户端如何与服务器进行连接呢？
 *                  连接N次后如果服务器连接不上，则表示服务器宕机，那么客户端等待指定的时间后再尝试连接
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
                                                new ReadTimeoutHandler(30),
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