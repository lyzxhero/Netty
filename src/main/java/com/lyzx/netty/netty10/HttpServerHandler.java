package com.lyzx.netty.netty10;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import  io.netty.handler.codec.http.HttpVersion;

import static io.netty.handler.codec.http.HttpHeaderNames.*;

public class HttpServerHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx,Object msg) throws Exception {
        FullHttpRequest request = (FullHttpRequest)msg;
        HttpMethod method = request.method();
        if(method != HttpMethod.POST){
            writeResponse(ctx,HttpResponseStatus.BAD_REQUEST, "错误的请求方式!");
            return;
        }

        ByteBuf byteBuf = request.content();
        byte[] bs = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bs);
        System.out.println("==>从客户端获取的数据:"+new String(bs));
        writeResponse(ctx,HttpResponseStatus.OK,"成功，哈哈！");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if(ctx.channel().isActive()){
            writeResponse(ctx,HttpResponseStatus.INTERNAL_SERVER_ERROR,"unKnowException");
        }
    }

    private void writeResponse(ChannelHandlerContext ctx,HttpResponseStatus status,String responseMsg){
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                status,
                Unpooled.copiedBuffer(responseMsg,CharsetUtil.UTF_8));
        response.headers()
                .set(HttpHeaderNames.CONTENT_TYPE,"text/json;charset=UTF-8")
                .set(ACCESS_CONTROL_ALLOW_ORIGIN,"*")
                .set(ACCESS_CONTROL_ALLOW_HEADERS,"Origin, X-Requested-With,Content-Type,Accept")
                .set(ACCESS_CONTROL_ALLOW_METHODS,"GET,POST,PUT,DELETE")
                .setInt(HttpHeaderNames.CONTENT_LENGTH,response.content().readableBytes())
                .set(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
        ctx.channel().writeAndFlush(response);
    }
}