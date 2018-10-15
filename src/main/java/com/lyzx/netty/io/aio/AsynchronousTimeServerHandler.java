package com.lyzx.netty.io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

public class AsynchronousTimeServerHandler implements Runnable{
	private int port;
	private CountDownLatch latch;
	private AsynchronousServerSocketChannel assc;

	public AsynchronousTimeServerHandler(int port){
		try {
			assc = AsynchronousServerSocketChannel.open();
			assc.bind(new InetSocketAddress(port));
			System.out.println("AIO 服务端绑定的端口是:"+port);
		} catch (IOException e) {
			System.out.println("AsynchronousTimeServerHandler 初始化失败.....");
			e.printStackTrace();
		}
	}


	@Override
	public void run() {
		latch = new CountDownLatch(1);
		doAccept();
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void doAccept(){
		//accept(A attachment, CompletionHandler<AsynchronousSocketChannel,? super A> handler)
		this.assc.accept(this,new AcceptCompletionHandler());
	}

	public int getPort() {
		return port;
	}

	public CountDownLatch getLatch() {
		return latch;
	}

	public AsynchronousServerSocketChannel getAssc() {
		return assc;
	}

}