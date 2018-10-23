package com.lyzx.netty.netty11;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;


public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    public void initChannel(SocketChannel ch){
        ch.pipeline()
            .addLast("httpRequestDecoder",new HttpRequestDecoder())
            .addLast("httpResponseEncoder",new HttpResponseEncoder())
            .addLast("httpObjectAggregator",new HttpObjectAggregator(65536))
            .addLast("serverHandler",new HttpServerHandler());
    }
}