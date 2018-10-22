package com.lyzx.netty.netty08;

public class GeneralMessage {
    private String header;
    private int code;
    private String data;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "GeneralMessage{" +
                "header='" + header + '\'' +
                ", code=" + code +
                ", data='" + data + '\'' +
                '}';
    }
}
