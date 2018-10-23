package com.lyzx.netty.netty10;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author hero.li
 *  演示netty开发Http协议
 *
 */
public class Server {

    public static void main(String[] args) throws InterruptedException {
        //开启两个线程组，一个用于接受客户端的请求   另一个用于异步的网络IO的读写
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childOption(ChannelOption.SO_KEEPALIVE,Boolean.TRUE)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>(){
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                                /**
                                 * 添加一系列编解码器
                                 * HttpRequestDecoder     ===> 对从客户端发来的请求解码
                                 * HttpObjectAggregator   ===> 将多个消息转换为单一的FullHttpRequest/FullHttpResponse 原因是Http解码器会在每个Http消息中生成多个消息对象
                                 *              HttpRequest/HttpResponse
                                 *              HttpContent
                                 *              LastHttpContent
                                 * HttpResponseEncoder    ===> 对响应消息做编码
                                 * ChunkedWriteHandler    ===> 用于异步发送大的码流但不占用过多的内存，防止jvm内存溢出
                                 */
                                ch.pipeline()
                                      .addLast("httpRequestDecoder",new HttpRequestDecoder())
                                      .addLast("httpObjectAggregator",new HttpObjectAggregator(65536))
                                      .addLast("httpResponseEncoder",new HttpResponseEncoder())
                                      .addLast("chunkedWriteHandler",new ChunkedWriteHandler())
                                      .addLast("serverHandler",new HttpServerHandler())
                                                ;
                        }
                    });

            //同步等待绑定端口结束
            ChannelFuture f = b.bind(9988).sync();
            //等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        }finally {
            //优雅的关闭线程组
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}