package com.lyzx.netty.netty08;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();

        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss,work)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childOption(ChannelOption.SO_KEEPALIVE,Boolean.TRUE)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
//                            ch.pipeline().addLast(new MessagePackEncoder());
//                            ch.pipeline().addLast(new MessagePackDecoder());

                            ch.pipeline().addLast(com.lyzx.netty.netty08.MarshallingCodeCFactory.marshallingDecoder());
                            ch.pipeline().addLast(com.lyzx.netty.netty08.MarshallingCodeCFactory.marshallingEncoder());
                            ch.pipeline().addLast(new ServerHandler());
                        }
                    });

            ChannelFuture ch = b.bind(9998).sync();
            ch.channel().closeFuture().sync();
        }finally{
            boss.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }
}
