package com.lyzx.netty.io.bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;

public class Server {
	
	public static void main(String[] args) {
		try {
			ServerSocketChannel ssc = ServerSocketChannel.open();
			ssc.bind(new InetSocketAddress(8895));
			
			int index = 0;
			while(true){
				SocketChannel sc = ssc.accept();
				new Thread(new Timer(sc),"00"+index).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


class Timer implements Runnable{
	private SocketChannel sc;
	
	public Timer(SocketChannel sc){
		this.sc = sc;
	}
	
	@Override
	public void run() {
		ByteBuffer buffer = ByteBuffer.allocate(512);
		try {
			int len = sc.read(buffer);
			buffer.flip();
			String line = new String(buffer.array(),0,len);
			buffer.clear();
			
			String currentTime = "QUERY TIME".equals(line) ? (new Date()).toString():"BAD REQUEST";
			buffer.put(currentTime.getBytes());
			buffer.flip();
			sc.write(buffer);
			sc.shutdownOutput();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(null != sc){
				try {
					sc.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
