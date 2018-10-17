package com.lyzx.netty.netty05;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client:channelActive____通道激活开始");
        for(int i=0;i<5;i++){
            NettyRequest req = new NettyRequest();
            req.setId((long)i);
            req.setMsg("data_"+i);
            ctx.channel().writeAndFlush(req);
            System.out.println("..."+req);
            Thread.sleep(2000);
        }
        System.out.println("client:channelActive____通道激活结束");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception {
        System.out.println("client____:通道可读开始");
        NettyRequest nr = (NettyRequest)msg;
        System.out.println("client____response time:"+nr);
        System.out.println("client____:通道可读结束");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client:通道可读完成");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("client:发生异常");

    }
}