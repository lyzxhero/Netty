package com.lyzx.netty.day01;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class ByteBufferTest {


    /**
     * 获取ByteBuffer的方式
     * 1、ByteBuffer.allocateDirect(1024)
     * 2、ByteBuffer.allocate(1024)
     * 3、ByteBuffer.wrap("你好".getBytes("UTF-8"))
     *
     * @throws UnsupportedEncodingException
     */
    @Test
    public void test1() throws UnsupportedEncodingException {

        ByteBuffer buffer1 = ByteBuffer.allocateDirect(1024);
        ByteBuffer buffer2 = ByteBuffer.allocate(1024);
        System.out.println(buffer1.isDirect()  +"     "+buffer2.isDirect());

        //默认是写模式
        System.out.println("init:"+buffer1.position()+"  "+buffer1.limit()+"    "+buffer1.capacity());
        buffer1.put("你好".getBytes("UTF-8"));
        System.out.println("put:"+buffer1.position()+"  "+buffer1.limit()+"    "+buffer1.capacity());
        buffer1.flip();
        System.out.println("flip:"+buffer1.position()+"  "+buffer1.limit()+"    "+buffer1.capacity());
        System.out.println("==================================================");


        //默认是写模式
        ByteBuffer buffer3 = ByteBuffer.wrap("你好".getBytes("UTF-8"));
        System.out.println("wrap_init:"+buffer3.position()+"  "+buffer3.limit()+"   "+buffer3.capacity()+"    "+buffer3.isReadOnly());
//        buffer3.put("中国".getBytes("UTF-8"));
//        System.out.println("put:"+buffer3.position()+"  "+buffer3.limit()+"   "+buffer3.capacity());
//        buffer3.flip();

        byte b1 = buffer3.get();
        byte b2 = buffer3.get();
        byte b3 = buffer3.get();
        byte[] b ={b1,b2,b3};
        System.out.println(new String(b));

        System.out.println("============================");
    }


    @Test
    public void test2(){

    }



}
