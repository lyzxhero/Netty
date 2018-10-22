package com.lyzx.netty.netty08;

import org.junit.Test;
import org.msgpack.MessagePack;
import org.msgpack.template.Templates;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 测试MessagePack序列化框架
 */
public class MessagePackTest{

    /**
     * 使用MessagePack进行编码和解码
     */
    @Test
    public void test1() throws IOException{
        MessagePack mp = new MessagePack();
        List<String> datas = new ArrayList<>();

        datas.add("A");
        datas.add("B");
        datas.add("C");
        datas.add("D");
        datas.add("E");

        byte[] datasBytes = mp.write(datas);
        System.out.println(datasBytes.length);

        List<String> read1 = mp.read(datasBytes, Templates.tList(Templates.TString));
        for(String item : read1){
            System.out.println("==="+item);
        }
    }

    @Test
    public void test2() throws IOException{
        MessagePack mp = new MessagePack();
        List<Integer> list = new LinkedList<>();//Arrays.asList(1, 2, 3, 5);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);

        byte[] ds = mp.write(list);
        System.out.println("ds.length="+ds.length);
        List<Integer> dList = mp.read(ds, Templates.tList(Templates.TInteger));
        for(Integer item : dList){
            System.out.println(item);
        }
    }


}
