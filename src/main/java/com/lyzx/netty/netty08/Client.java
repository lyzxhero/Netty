package com.lyzx.netty.netty08;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.InetSocketAddress;

public class Client {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup w = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(w)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.TCP_NODELAY,true)
            .handler(new ChannelInitializer<SocketChannel>(){
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
//                    ch.pipeline().addLast(new MessagePackEncoder());
//                    ch.pipeline().addLast(new MessagePackDecoder());

                    ch.pipeline().addLast(MarshallingCodeCFactory.marshallingDecoder());
                    ch.pipeline().addLast(MarshallingCodeCFactory.marshallingEncoder());
                    ch.pipeline().addLast(new ClientHandler());
                }
            });

        ChannelFuture cf = b.connect(new InetSocketAddress("127.0.0.1", 9998)).sync();

        GeneralMessage g = new GeneralMessage();
        g.setCode(1);
        g.setData("你好，我是一个普通的消息");
        g.setHeader("i am a head");
        cf.channel().writeAndFlush(g);
        System.out.println("客户端发送的消息:"+g);

        cf.channel().closeFuture().sync();
        w.shutdownGracefully();
    }
}