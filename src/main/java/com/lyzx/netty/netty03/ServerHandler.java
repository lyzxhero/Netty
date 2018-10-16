package com.lyzx.netty.netty03;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;


/**
 * 对于网事件做读写，通常只要关注channelRead()和exceptionCaught()即可
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server:channelRead____通道可读开始");
        //从msg对象中获取消息并打印
        ByteBuf buff = (ByteBuf)msg;
        byte[] bytes = new byte[buff.readableBytes()];
        buff.readBytes(bytes);
        System.out.println("server:收到的消息____:"+new String(bytes).trim());
        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS"));

        char[] responseCharArrData = new char[64];
        Arrays.fill(responseCharArrData,' ');
        for(int i=0,count=datetime.length();i<count;i++){
            responseCharArrData[i] = datetime.charAt(i);
        }

        String responseData = new String(responseCharArrData);
        ctx.writeAndFlush(Unpooled.copiedBuffer(responseData.getBytes()));
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