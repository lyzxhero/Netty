package com.lyzx.netty.netty04;

import java.io.Serializable;

public class NettyRequest implements Serializable {

    private Long id;
    private int code;
    private String msg;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "NettyRequest{" +
                "id=" + id +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

}
