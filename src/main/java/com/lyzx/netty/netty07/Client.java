package com.lyzx.netty.netty07;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;


public class Client {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>(){
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception{
                            ChannelHandler[] arr = {MarshallingCodeCFactory.marshallingDecoder(),
                                                    MarshallingCodeCFactory.marshallingEncoder(),
                                                    new ReadTimeoutHandler(30),
                                                    new ClientHandler()};
                            ch.pipeline().addLast(arr);
                    }
                });
        ChannelFuture f = b.connect("127.0.0.1", 9988).sync();

        f.channel().closeFuture().sync();
        group.shutdownGracefully();
    }
}