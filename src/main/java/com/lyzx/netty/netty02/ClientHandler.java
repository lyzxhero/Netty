package com.lyzx.netty.netty02;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client:channelActive  通道激活开始");
        for(int i=0;i<10;i++){
            //以$$_$$为分隔符，所以在每次发送的时候都需要以消息末尾加上$$_$$
            ByteBuf buff  = Unpooled.copiedBuffer(("req_"+i+"$$_$$").getBytes("UTF-8"));
            ctx.writeAndFlush(buff);
        }
        System.out.println("client:channelActive  通道激活结束");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception {
        System.out.println("client:通道可读开始");
        ByteBuf buf = (ByteBuf)msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        System.out.println("response time :"+new String(bytes));
        System.out.println("client:通道可读结束");
    }

    /**
     * 类似于AOP的后置通知 在这里当通道读取完毕后关闭通道
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client:通道可读完成");
//        ctx.channel().close();
//        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("client:发生异常");
        super.exceptionCaught(ctx, cause);
    }
}