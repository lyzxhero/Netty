package com.lyzx.netty.netty07.messageModel;

public class RequestMessage {
    private RequestHead head;
    private Object data;

    public RequestHead getHead() {
        return head;
    }

    public void setHead(RequestHead head) {
        this.head = head;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


}
