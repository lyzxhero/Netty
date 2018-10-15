package com.lyzx.netty.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Client {

	public static void main(String[] args) {
		try {
			SocketChannel sc = SocketChannel.open(new InetSocketAddress("100.100.100.100",8895));
			sc.configureBlocking(false);
			ByteBuffer buffer = ByteBuffer.wrap("QUERY TIME".getBytes());
			sc.write(buffer);
			buffer.clear();
			sc.shutdownOutput();

			Selector selector = Selector.open();

			/**
			 * 注册通道到选择器上的通道都必须是异步的
			 */
			sc.register(selector, SelectionKey.OP_READ);

			if(selector.select() > 0){
				Iterator<SelectionKey> itr = selector.selectedKeys().iterator();
				while(itr.hasNext()){
					SelectionKey sk = itr.next();
					itr.remove();

					if(sk.isValid()){
						if(sk.isReadable()){
							/**
							 * 此Channel就是连接服务器时的Channel
							 * SocketChannel sc2 = (SocketChannel)sk.channel();
							 */
							int len = sc.read(buffer);
							buffer.flip();
							System.out.println("client,currentTime"+new String(buffer.array(),0,len));
							buffer.clear();
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

