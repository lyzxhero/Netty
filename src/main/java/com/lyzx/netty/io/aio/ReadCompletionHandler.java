package com.lyzx.netty.io.aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;


/**
 *
 * @author MECHREVO
 */
public class ReadCompletionHandler implements CompletionHandler<Integer,ByteBuffer> {
	private AsynchronousSocketChannel asc;

	public ReadCompletionHandler(AsynchronousSocketChannel asc){
		if(null == this.asc){
			this.asc = asc;
		}
	}


	@Override
	public void completed(Integer result, ByteBuffer attachment) {
		attachment.flip();
		byte[] buf = new byte[attachment.remaining()];
		attachment.get(buf);

		try {
			String order = new String(buf);
			String currentTime = "QUERY".equals(order)?(new Date()).toString():"BAD QUERY";
			doWrite(currentTime);
		}catch(Exception e) {
			e.printStackTrace();
		}


	}

	@Override
	public void failed(Throwable exc, ByteBuffer attachment) {
		System.out.println("ReadCompletionHandler.failed.读取失败.....");
		exc.printStackTrace();
		try{
			this.asc.close();
		}catch(IOException e){
			e.printStackTrace();
		}

	}

	public void doWrite(String curTime){
		ByteBuffer buffer = ByteBuffer.wrap(curTime.getBytes());
		this.asc.write(buffer,buffer,new CompletionHandler<Integer,ByteBuffer>() {

			@Override
			public void completed(Integer result, ByteBuffer attachment) {
				if(attachment.hasRemaining()){
					asc.write(attachment, attachment, this);
				}
			}

			@Override
			public void failed(Throwable exc, ByteBuffer attachment) {
				exc.printStackTrace();
				attachment.clear();
				try{
					asc.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		});

	}
}