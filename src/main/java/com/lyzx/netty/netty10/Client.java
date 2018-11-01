package com.lyzx.netty.netty10;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

public class Client {
    private static  final  String targetHost = "127.0.0.1";
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.TCP_NODELAY,true)
            .handler(new ChannelInitializer<SocketChannel>(){
                @Override
                protected void initChannel(SocketChannel ch) throws Exception{
                    ch.pipeline()
                        .addLast(new HttpClientCodec())
                        .addLast(new HttpContentDecompressor())
                            .addLast("httpObjectAggregator",new HttpObjectAggregator(65536))
                        .addLast("HttpClientHandler",new HttpClientHandler(targetHost));
                }
            });

        ChannelFuture f = b.connect(targetHost, 9988).sync();
        f.channel().closeFuture().sync();
        group.shutdownGracefully();
    }

}