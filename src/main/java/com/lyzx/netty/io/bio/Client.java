package com.lyzx.netty.io.bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 这个用最传统的BIO模拟了时间服务器
 * 使用阻塞式IO请求服务器，服务器接受到了请求就分配一个线程给该请求用于处理该线程
 * 这种做法的缺点是阻塞，如果有很多很慢的客户端请求服务器时，很多线程都会被阻塞，从而导致服务器性能急剧下降
 * 客户端也感觉到访问时间服务器很慢
 * @author lyzx_hero
 */
public class Client {
	
	public static void main(String[] args) {
		try {
			SocketChannel sc = SocketChannel.open(new InetSocketAddress("100.100.100.100",8895));
			ByteBuffer buffer = ByteBuffer.allocate(512);
			buffer.put("QUERY TIME".getBytes());
			buffer.flip();
			sc.write(buffer);
			sc.shutdownOutput();
			
			buffer.clear();
			int len = sc.read(buffer);
			buffer.flip();
			String currentTime = new String(buffer.array(),0,len);
			System.out.println(currentTime);
			
			sc.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
