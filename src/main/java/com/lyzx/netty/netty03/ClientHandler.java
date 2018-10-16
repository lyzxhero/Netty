package com.lyzx.netty.netty03;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.Arrays;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client:channelActive____通道激活开始");
        for(int i=0;i<20;i++){
            char[] charArr = new char[64];
            Arrays.fill(charArr,' ');
            String req = "request0000000000_"+i;
            for(int j=0,count=req.length();j<count;j++){
                charArr[j] = req.charAt(j);
            }
            String reqInfo = new String(charArr);
            System.out.println("client:====================="+reqInfo+"    "+reqInfo.length());
            ctx.channel().writeAndFlush(Unpooled.copiedBuffer(reqInfo.getBytes()));
        }
        System.out.println("client:channelActive____通道激活结束");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception {
        System.out.println("client____:通道可读开始");
        ByteBuf buff = (ByteBuf)msg;
        byte[] bytes = new byte[buff.readableBytes()];
        buff.readBytes(bytes);
        System.out.println("client____response time:"+new String(bytes).trim());
        System.out.println("client____:通道可读结束");
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
    }


//    public static void main(String[] args) {
//        char[] arr = new char[128];
//        Arrays.fill(arr,' ');
//        arr[0]='1';
//        arr[1]='2';
//        arr[2]='3';
//        arr[3]='4';
//
//        System.out.println(new String(arr)+"  |");
//    }
}