package com.lyzx.netty.io.aio;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FilterWriter;
import java.io.IOException;
import java.util.LinkedHashMap;

public class Client {
    private static BufferedWriter bw;

    static{
        try {
            bw = new BufferedWriter(new FileWriter("1.txt", true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void t1() throws IOException{

        bw.write("555555");
        bw.newLine();
        bw.write("2222");
        bw.newLine();
        bw.write("3333");
        bw.newLine();
        bw.close();
    }

    public static void main(String[] args)  {

        try{
            t1();
            t1();
            t1();
            t1();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
