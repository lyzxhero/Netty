package com.lyzx.netty.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;


public class NewClient implements Runnable{
	private Selector selector;
	private SocketChannel sc;
	private String hostName;
	private int port;

	public NewClient(String hostName,int port){
		this.hostName = hostName;
		this.port = port;

		try {
			this.selector = Selector.open();
			sc = SocketChannel.open(new InetSocketAddress(this.hostName,this.port));
			sc.configureBlocking(false);
			sc.register(this.selector,SelectionKey.OP_READ);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void run(){
		Scanner scanner = new Scanner(System.in);

		int index = 3;
		try {
			while(index-- > 0){
				ByteBuffer buffer = ByteBuffer.allocate(512);
				String line = scanner.next();
				buffer.put(line.getBytes());
				buffer.flip();
				sc.write(buffer);
				buffer.clear();

				//轮询出准备好的Channel（以SelectKey的形式）个数
				int sum = selector.select();
				if(sum > 0){
					Iterator<SelectionKey> itr = selector.selectedKeys().iterator();
					while(itr.hasNext()){
						SelectionKey key = itr.next();
						itr.remove();
						if(key.isValid()){
							if(key.isReadable()){
								SocketChannel sc = (SocketChannel)key.channel();
								int len = sc.read(buffer);
								if(len > 0){
									String response = new String(buffer.array(),0,len);
									System.out.println("客户端收到的消息:"+response);
								}
							}
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			scanner.close();

			if(null != sc){
				try {
					sc.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if(null != selector){
				try {
					selector.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		new Thread(new NewClient("localhost",9876) ).start();



//		try(SocketChannel sc = SocketChannel.open(new InetSocketAddress("localhost",9876))) {
//			Scanner scanner = new Scanner(System.in);
//			int index = 3;
//			while(index-- > 0){
//				ByteBuffer buffer = ByteBuffer.allocate(512);
//				String line = scanner.next();
//
//				buffer.put(line.getBytes());
//				buffer.flip();
//				sc.write(buffer);
//				buffer.clear();
//				int len = sc.read(buffer);
//				if(len > 0){
//					String time = new String(buffer.array(),0,len);
//					System.out.println("客户端收到的服务器端的消息:"+time);
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

	}


}
