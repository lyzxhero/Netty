package com.lyzx.netty.netty07;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 对于网事件做读写，通常只要关注channelRead()和exceptionCaught()即可
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static final String AUTH_SUCCESS_FLAG = "AUTH_SUCCESS";
    private static final String AUTH_FAIL_FLAG = "AUTH_FAIL";

    private static final Map<String,String> KEYS = new ConcurrentHashMap<>();
    static{
        //这儿本应该读取数据库以初始化可以访问该服务器的客户端
        KEYS.put("192.168.22.170","abcd007");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg.getClass() == String.class){
            //刚建立连接时的握手信息
            String ipAndSuffix = String.valueOf(msg);
            String[] ipAndSuffiArr = ipAndSuffix.split("_");
            String ip = ipAndSuffiArr[0];
            String suffix  = ipAndSuffiArr[1];
            System.out.println("ip:"+ip+"   ,  "+suffix);
            if(KEYS.containsKey(ip)){
                if(suffix.equals(KEYS.get(ip))){
                    ctx.channel().writeAndFlush(AUTH_SUCCESS_FLAG);
                    return;
                }
            }
            ctx.channel().writeAndFlush(AUTH_FAIL_FLAG).addListener(ChannelFutureListener.CLOSE);
        }else{
            System.out.println("server:channelRead____通道可读开始");
            NettyRequest nr = (NettyRequest)msg;
            System.out.println("server:收到的消息____:"+nr);

            String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS"));
            nr.setMsg(datetime);
            ctx.channel().writeAndFlush(nr);
            System.out.println("server:channelRead____通道可读结束");
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server:channelReadComplete____通道可读完成 ");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("server:exceptionCaught____发生异常");
        ctx.close();
    }

}