package com.lyzx.netty.netty02;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.junit.Test;

/**
 * @author hero.li
 * 测试netty的拆包粘包问题
 * 拆包粘包的出现的底层原因是
 * TCP协议只是一个传输控制协议，TCP协议只需要把数据在客户端和服务端之间传输
 * 而并不知道这些数据的具体含义，就像水流一样，只要把数据传输成功就完成任务
 * 对于数据可能按照自己的一些规则组合更高效的传输，在组合的时候就无意中改变了
 * 上层业务数据的"规则" 这就是拆包粘包问题
 *
 * 解决方案有3 种
 * 1、分隔符
 *      只需要在客户端和服务端的消息过滤器上加上DelimiterBasedFrameDecoder的实现即可
 *      注意:在发送消息时就需要加上分隔符(客户端和服务端都要加)
 * 2、消息定长
 * 3、自定义协议，报文头和报文体，其中报文头记录报文体的长度
 */
public class Netty02 {
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
                        //以$$_$$为分隔符，
                        ByteBuf delimiter = Unpooled.copiedBuffer("$$_$$".getBytes("UTF-8"));
                        socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,delimiter));
                        socketChannel.pipeline().addLast(new StringEncoder());
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

                        ByteBuf delimiter = Unpooled.copiedBuffer("$$_$$".getBytes("UTF-8"));
                        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,delimiter));
                        ch.pipeline().addLast(new StringEncoder());
                        ch.pipeline().addLast(new ClientHandler());
                    }
                });

        ChannelFuture f = b.connect("127.0.0.1", 9988).sync();
        f.channel().closeFuture().sync();
        group.shutdownGracefully();
    }
}