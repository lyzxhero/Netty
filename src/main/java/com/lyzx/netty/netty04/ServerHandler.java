package com.lyzx.netty.netty04;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * 对于网事件做读写，通常只要关注channelRead()和exceptionCaught()即可
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server:channelRead____通道可读开始");
        //从msg对象中获取消息并打印
        NettyRequest nr = (NettyRequest)msg;
        System.out.println("server:收到的消息____:"+nr);

        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS"));
        nr.setMsg(datetime);
        ctx.channel().writeAndFlush(nr);
        System.out.println("server:channelRead____通道可读结束");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server:channelReadComplete____通道可读完成 ");
//        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("server:exceptionCaught____发生异常");
        ctx.close();
    }

}