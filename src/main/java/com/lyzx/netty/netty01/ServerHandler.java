package com.lyzx.netty.netty01;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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
        System.out.println("server:channelRead 通道可读开始");
        //从msg对象中获取消息并打印
        ByteBuf bytebuf = (ByteBuf)msg;
        byte[] bytes = new byte[bytebuf.readableBytes()];
        bytebuf.readBytes(bytes);
        System.out.println("收到的消息:"+new String(bytes,"UTF-8"));

        //获取当前的时间并通过ctx的write方法异步写数据到SocketChannel
        //ctx.write并不会直接把数据写入到缓冲区等到调用channelReadComplete里面的ctx.flush()
        //的时候再把数写入到SocketChannel
        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS"));
        ByteBuf byteBuf = Unpooled.copiedBuffer(datetime.getBytes("UTF-8"));
        ctx.writeAndFlush(byteBuf);
        System.out.println("server:channelRead 通道可读结束");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server:channelReadComplete 通道可读完成 ");
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("server:exceptionCaught 发生异常");
        ctx.close();
    }
}
