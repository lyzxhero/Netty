package com.lyzx.netty.netty08;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client:通道激活开始");

        System.out.println("client:通道激活结束");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("client:通道可读开始");
//        ByteBuf message = (ByteBuf)msg;
//        byte[] bytes = new byte[message.readableBytes()];
//        message.readBytes(bytes);
//        System.out.println("客户端收到的信息:"+new String(bytes));
//        System.out.println("client:通道可读结束");

        GeneralMessage g = (GeneralMessage)msg;
        System.out.println("client received:"+g);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client:通道可读完成");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        System.out.println("client:通道异常");
    }
}
