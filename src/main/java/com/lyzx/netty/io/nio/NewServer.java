package com.lyzx.netty.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;


/**
 *  IO和NIO的区别
 *  1、IO采用流技术是单向的
 *     NIO采用Buffer、Channel、Selector技术是双向的，效率高
 *  2、NIO可以采用非阻塞式但是IO只能是阻塞式
 *
 *
 *  使用NIO做客户端服务器端的通信
 *  服务器端
 *  	1、创建serverSocketChannel ssc
 *  	2、设置为非阻塞
 *  	3、绑定服务器端的端口
 *  	4、创建selector
 *  	5、使用ssc注册selector
 *  	6、轮询selector找出准备好的Channel
 *  	7、如果可以接收状态（isAcceptable）接受客户端Channel并注册到selector
 *  	8、如果是可读状态（isReadable）直接读取内容到缓冲区并写数据给客户端（Channel是双向的当可读时也能保证可写）
 * @author lyzx_hero
 */
public class NewServer implements Runnable{
	private Selector selector;
	private ServerSocketChannel ssc;
	private int port;


	public NewServer(int port){
		this.port = port;
		try {
			//1、获取多路复用器
			selector = Selector.open();

			//2、打开服务器端通道
			ssc = ServerSocketChannel.open();
			//3、设置服务器通道为非阻塞模式
			ssc.configureBlocking(false);
			//4、为服务器通道绑定服务器的端口
			ssc.bind(new InetSocketAddress("localhost",this.port));
			//5、把服务器通道注册到选择器上
			ssc.register(selector, SelectionKey.OP_ACCEPT);
		}catch (IOException e) {
			e.printStackTrace();
		}

	}


	@Override
	public void run() {
		while(true){
			try {
				//1、轮询selector以获取准备好的Channel(以SelectKey的形式)
				int num = selector.select();
				if(num > 0){
					//2、这个Iterator包含了所有准备好的Channel（某一方面准备好了比如可读/可写/可接受）
					Iterator<SelectionKey> itr = selector.selectedKeys().iterator();
					while(itr.hasNext()){
						/**
						 * 3、这个是一个在某方面准备好了的SelectionKey，刚获取他的引用后立即把它从Set中删除，当它下一次要使用的时候让该通道再次注册就行
						 *  记住这个删除只是把该通道的某一中属性删除（比如删除了A通道的可读属性）
						 */
						SelectionKey key = itr.next();
						itr.remove();
						readHandler(key);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * 这个类用来判断各个通道的状态以及对于不同的状态做出不同的处理
	 * @param key
	 */
	public void readHandler(SelectionKey key){
		if(!key.isValid()){
			return ;
		}

		if(key.isAcceptable()){
			ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
			try {
				SocketChannel sc = ssc.accept();
				sc.configureBlocking(false);
				sc.register(this.selector,SelectionKey.OP_READ);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if(key.isReadable()){
			SocketChannel sc = (SocketChannel)key.channel();
			ByteBuffer buffer = ByteBuffer.allocate(1024);

			try {
				int len = sc.read(buffer);
				if(len <= 0){
					key.cancel();
					sc.close();
					return ;
				}

				String order = new String(buffer.array(),0,len);
				System.out.println("服务器收到的信息:"+order);

				String currentTime = "QUERY".equals(order)?(new Date()).toString():"BAD QUERY";
				ByteBuffer response = ByteBuffer.wrap(currentTime.getBytes());
				sc.write(response);
			}catch(IOException e){
				try {
					/**
					 * 如果发生异常就关闭这个Channel
					 * 并取消这个Channel对应的Key的所有事件
					 */
					sc.close();
					key.cancel();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.out.println("XXXXXXXX");
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new Thread(new NewServer(9876)).start();
	}

}

