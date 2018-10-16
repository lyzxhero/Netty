package com.lyzx.netty.netty04;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


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
//                        ChannelHandler[] arr = {MarshallingCodeCFactory.buildMarshallingDecoder(),
//                                                MarshallingCodeCFactory.buildMarshallingEncoder(),
//                                                new ClientHandler()};
//                        ch.pipeline().addLast(arr);

                        ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                        ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                        ch.pipeline().addLast(new ClientHandler());
                    }
                });
        ChannelFuture f = b.connect("127.0.0.1", 9988).sync();


        for(int i=0;i<10;i++){
            NettyRequest req = new NettyRequest();
            req.setId((long)i);
            req.setMsg("data_"+i);
            f.channel().writeAndFlush(req);
            System.out.println("..."+req);
        }

        f.channel().closeFuture().sync();
        group.shutdownGracefully();
    }

}