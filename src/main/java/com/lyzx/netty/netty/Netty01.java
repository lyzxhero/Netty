package com.lyzx.netty.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.Test;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

/**
 * @author hero.li
 * 第一个Netty程序
 */
public class Netty01 {

    @Test
    public void nettyServer() throws InterruptedException{
        //开启两个线程组，一个用于接受客户端的请求   另一个用于网络IO的读写
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024)
                .childHandler(new ServerChildHandlerImpl());

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

class ServerChildHandlerImpl extends ChannelInitializer<SocketChannel>{

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        System.out.println("server:initChannel 通道初始化");
        ch.pipeline().addLast(new ServerTimeServerHandler());
    }
}

/**
 * 对于网事件做读写，通常只要关注channelRead()和exceptionCaught()即可
 */
class ServerTimeServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server:channelRead 通道可读开始");
        //从msg对象中获取消息并打印
        ByteBuf bytebuf = (ByteBuf)msg;
        byte[] bytes = new byte[bytebuf.readableBytes()];
        bytebuf.readBytes(bytes);
        System.out.println("收到的消息:"+new String(bytes,"UTF-8"));

        //获取当前的时间并通过ctx的write方法异步写数据到SocketChannel
        //ctx.write并不会直接把数据写入到缓冲区等到调用channelReadComplete里面的ctx.flush()
        //的时候再把数写入到SocketChannel
        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS"));
        ByteBuf byteBuf = Unpooled.copiedBuffer(datetime.getBytes("UTF-8"));
        ctx.write(byteBuf);
        System.out.println("server:channelRead 通道可读结束");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server:channelReadComplete 通道可读完成 ");
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("server:exceptionCaught 发生异常");
        ctx.close();
    }
}


class ClientHandler extends ChannelInboundHandlerAdapter{
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client:channelActive  通道激活开始");
        ByteBuf buff  = Unpooled.copiedBuffer("11".getBytes("UTF-8"));
        ctx.writeAndFlush(buff);
        System.out.println("client:channelActive  通道激活结束");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception {
        System.out.println("client:通道可读开始");
        ByteBuf buf = (ByteBuf)msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        System.out.println("response time :"+new String(bytes));
        System.out.println("client:通道可读结束");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client:通道可读完成");
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("client:发生异常");
        super.exceptionCaught(ctx, cause);
    }
}