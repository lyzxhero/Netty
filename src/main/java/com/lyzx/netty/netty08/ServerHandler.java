package com.lyzx.netty.netty08;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server:通道激活");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf message = (ByteBuf)msg;
//        byte[] bytes = new byte[message.readableBytes()];
//        message.readBytes(bytes);
//        System.out.println("服务端收到的消息:"+new String(bytes));
//        ctx.channel().writeAndFlush(Unpooled.copiedBuffer("来自服务端的反馈消息".getBytes("UTF-8")));
//        System.out.println("server:通道可读");

        System.out.println("server端,接收的消息:"+msg);
        GeneralMessage g = (GeneralMessage)msg;
        System.out.println("server received:"+g);
        ctx.channel().writeAndFlush(g);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server:通道可读完成");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("server:异常");
        cause.printStackTrace();
    }
}