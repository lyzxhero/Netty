package com.lyzx.netty.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * 使用NIO创建时间服务器
 * 客户端和服务端都使用NIO非阻塞式交互
 * 把连接交给Selector，当有Channel可以接受了(isAccept=true)，可读(isReadable=true),可写了(isWritable=true)时把准备好的Channel交给
 * 指定的线程做相应的处理
 * @author lyzx_hero
 */
public class Server {

	public static void main(String[] args) {
//		try {
//			PrintStream out = new PrintStream("1.txt");
//			System.setOut(out);
//		}catch(FileNotFoundException e1){
//			e1.printStackTrace();
//		}

		try {
			ServerSocketChannel ssc = ServerSocketChannel.open();
			ssc.configureBlocking(false);
			ssc.socket().bind(new InetSocketAddress("100.100.100.100",8895));
			Selector selector = Selector.open();
			ssc.register(selector, SelectionKey.OP_ACCEPT);
			String currentTime = "";
			while(true){
				int sum = selector.select();
				if(sum > 0){
					System.out.println("selector.select="+sum);
					Set<SelectionKey> sets = selector.selectedKeys();
					Iterator<SelectionKey> itr = sets.iterator();

					while(itr.hasNext()){
						SelectionKey sk = itr.next();
//						sk.cancel();
//						System.out.println("sk="+sk);
						itr.remove();
//						System.out.println("itr.remove......");

						try {
							if(sk.isValid()){
								if(sk.isAcceptable()){
									System.out.println("sk.isAcceptable。。。。。。。");
									SocketChannel sc = ssc.accept();
									SocketAddress address = sc.getLocalAddress();
									System.out.println("address="+address);

									sc.configureBlocking(false);
									sc.register(selector, SelectionKey.OP_READ);
								}

								if(sk.isReadable()){
									System.out.println("sk.isReadable..........");
									SocketChannel sc = (SocketChannel)sk.channel();
									System.out.println("sc="+sc);
									ByteBuffer buffer = ByteBuffer.allocate(512);
									int len = sc.read(buffer);
									if(len > 0){
										buffer.flip();
										String order = new String(buffer.array(),0,len);
										currentTime = "QUERY TIME".equals(order) ? (new Date()).toString():"BAD QUERY";
										System.out.println("currentTime="+currentTime+"   order="+order);
										buffer.clear();

										/**
										 * Channel时双向的当可读的时候也可以保证可写
										 */
										ByteBuffer bf = ByteBuffer.wrap(currentTime.getBytes());
										sc.write(bf);
									}else{
										System.out.println("sk.cancel....sc.close");
										sk.cancel();
										sc.close();
									}
								}
							}
						}catch(IOException e){
							e.printStackTrace();
						}
					}
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}

class X implements Runnable{
	private String currentTime = "";
	private Selector selector;

	public X(Selector selector){
		this.selector = selector;
	}

	@Override
	public void run() {
		Iterator<SelectionKey> itr = selector.selectedKeys().iterator();
		while(itr.hasNext()){
			SelectionKey sk = itr.next();
			try {
				if(sk.isAcceptable()){
					System.out.println("sk.isAcceptable。。。。。。。");
					ServerSocketChannel ssc = (ServerSocketChannel)sk.channel();
					SocketChannel sc = ssc.accept();
					sc.configureBlocking(false);
					sc.register(selector, SelectionKey.OP_READ);
				}else if(sk.isReadable()){
					System.out.println("sk.isReadable..........");
					SocketChannel sc = (SocketChannel)sk.channel();
					ByteBuffer buffer = ByteBuffer.allocate(512);
					int len = -1;
					while((len = sc.read(buffer)) != -1){
						buffer.flip();
						String order = new String(buffer.array(),0,len);
						currentTime = "QUERY TIME".equals(order) ? (new Date()).toString():"BAD QUERY";
						System.out.println("currentTime="+currentTime);
						buffer.clear();
					}
					sc.register(selector, SelectionKey.OP_WRITE);
				}else if(sk.isWritable()){
					System.out.println("sk.isWritable........");
					SocketChannel sc = (SocketChannel)sk.channel();
					ByteBuffer buffer = ByteBuffer.allocate(512);
					buffer.put(currentTime.getBytes());
					buffer.flip();
					sc.write(buffer);
					System.out.println("writed......");
				}
				itr.remove();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("itr.remove......");
		}
	}

}