package com.lyzx.netty.netty06;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetAddress;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    private static final String AUTH_SUCCESS_FLAG = "AUTH_SUCCESS";
    private static final String AUTH_FAIL_FLAG = "AUTH_FAIL";
    private String auth_suffix = "abcd007";

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client:channelActive____通道激活开始");
        String ip = InetAddress.getLocalHost().getHostAddress();
        String auth_key = ip+"_"+auth_suffix;
        ctx.channel().writeAndFlush(auth_key);
        System.out.println("client:channelActive____通道激活结束");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception {
        if(msg.getClass() == String.class){
            if(AUTH_SUCCESS_FLAG.equals(msg)){
                new Thread(new Scheduler(ctx)).start();
            }else{
                System.out.println("========认证失败:"+AUTH_FAIL_FLAG);
            }
        }else{
            NettyRequest nr = (NettyRequest)msg;
            System.out.println("client____response time:"+nr);
        }
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

class Scheduler implements Runnable{
    private  ChannelHandlerContext ctx;

    public Scheduler(ChannelHandlerContext ctx){
        this.ctx = ctx;
    }

    @Override
    public void run() {
//模拟定时发送心跳消息
        for(int i=0;i<20;i++){
            NettyRequest nr = new NettyRequest();
            nr.setId((long)i);
            nr.setCode(i);
            nr.setMsg("data_data:"+i);
            ctx.channel().writeAndFlush(nr);
            try {Thread.sleep(2000);
            }catch(InterruptedException e){e.printStackTrace();}
        }
    }
}