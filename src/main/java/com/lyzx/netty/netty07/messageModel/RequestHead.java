package com.lyzx.netty.netty07.messageModel;

public class RequestHead {
    private int crcCode ;       //消息id
    private int length;         //消息的总长度  header + body
    private long sessionID; 	//会话ID
    private byte type ;		    //消息的类型

    public int getCrcCode() {
        return crcCode;
    }

    public void setCrcCode(int crcCode) {
        this.crcCode = crcCode;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getSessionID() {
        return sessionID;
    }

    public void setSessionID(long sessionID) {
        this.sessionID = sessionID;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }


}
