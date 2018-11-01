package com.lyzx.netty.netty10;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class HttpClientHandler extends ChannelInboundHandlerAdapter {
    private String host ;
    public HttpClientHandler(String host){
        this.host = host;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client:channelActive____通道激活开始");
        for(int i=1;i<=100;i++){
            FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,HttpMethod.POST,host);
            request.headers().set(HttpHeaderNames.HOST,host);
            request.headers().add(HttpHeaderNames.CONTENT_TYPE, "application/json");
            String message = "{\"k1\":\"来自客户端的访问:\""+i+"}";
            ByteBuf bbuf = Unpooled.copiedBuffer(message,StandardCharsets.UTF_8);
            request.headers().set(HttpHeaderNames.CONTENT_LENGTH,bbuf.readableBytes());
            request.content().clear().writeBytes(bbuf);
            ctx.writeAndFlush(request);
            TimeUnit.MILLISECONDS.sleep(10);
        }
        System.out.println("client:channelActive____通道激活结束");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception {
        FullHttpResponse r = (FullHttpResponse)msg;
        ByteBuf buff = r.content();
        byte[] bytes = new byte[buff.readableBytes()];
        buff.readBytes(bytes);
        System.out.println("来自服务端的响应:"+new String(bytes).trim());
    }

    /**
     * 类似于AOP的后置通知 在这里当通道读取完毕后关闭通道
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client:通道可读完成");
        ctx.channel().closeFuture();
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
        System.err.println("client:发生异常");
        e.printStackTrace();
    }

}