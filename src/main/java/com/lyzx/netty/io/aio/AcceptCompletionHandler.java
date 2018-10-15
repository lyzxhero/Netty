package com.lyzx.netty.io.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * 这个类负责接收客户端<b>AsynchronousSocketChannel</b><br/>
 * 参数怎么确定暂时还不懂
 * @author MECHREVO
 */
public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel,AsynchronousTimeServerHandler>{

	@Override
	public void completed(AsynchronousSocketChannel result, AsynchronousTimeServerHandler attachment){
		/**
		 * 这样做是为了让该服务端通道再次异步的接收新来的客户端请求
		 * 从而形成一个循环
		 */
		attachment.getAssc().accept(attachment,this);
		System.out.println("AcceptCompletionHandler.completed,AsynchronousSocketChannel:"+result);
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		result.read(buffer,buffer, new ReadCompletionHandler(result));
	}

	@Override
	public void failed(Throwable exc, AsynchronousTimeServerHandler attachment) {
		exc.printStackTrace();
		attachment.getLatch().countDown();
	}
}