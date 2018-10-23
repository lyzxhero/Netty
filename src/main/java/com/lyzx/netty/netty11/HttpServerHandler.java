package com.lyzx.netty.netty11;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import java.net.InetSocketAddress;
import java.util.Map;
import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpServerHandler extends SimpleChannelInboundHandler<Object> {

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
		System.out.println("---"+msg.getClass().getName());

		FullHttpRequest request = (FullHttpRequest)msg;

		ByteBuf content = request.content();
		byte[] bytes = new byte[content.readableBytes()];
		content.readBytes(bytes);
		System.out.println("===="+new String(bytes));
	}

	private static void appendDecoderResult(StringBuilder buf, HttpObject o) {
		DecoderResult result = o.decoderResult();
		if (result.isSuccess()) {
			return;
		}

		buf.append(".. WITH DECODER FAILURE: ");
		buf.append(result.cause());
		buf.append("\r\n");
	}


	private void writeResponse(String processResult,ChannelHandlerContext ctx){
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK,Unpooled.copiedBuffer(processResult,CharsetUtil.UTF_8));
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/json; charset=UTF-8");
		response.headers().set(ACCESS_CONTROL_ALLOW_ORIGIN,"*");
        response.headers().set(ACCESS_CONTROL_ALLOW_HEADERS,"Origin, X-Requested-With, Content-Type, Accept");
        response.headers().set(ACCESS_CONTROL_ALLOW_METHODS,"GET,POST,PUT,DELETE");
        response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
		ctx.writeAndFlush(response);
	}

	private static void send100Continue(ChannelHandlerContext ctx) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,CONTINUE );
		ctx.write(response);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}