package com.lyzx.netty.io.aio;

public class Server {
	public static void main(String[] args) {
		new Thread(new AsynchronousTimeServerHandler(9876)).start();
	}
}
