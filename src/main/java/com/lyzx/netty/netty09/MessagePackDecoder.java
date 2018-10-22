package com.lyzx.netty.netty09;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;
import org.msgpack.type.Value;

import java.util.List;

public class MessagePackDecoder extends MessageToMessageDecoder<ByteBuf>{
    private static final MessagePack MESSAGE_PACK = new MessagePack();

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int len = byteBuf.readableBytes();
        byte[] bytes = new byte[len];
        byteBuf.getBytes(byteBuf.readerIndex(),bytes,0,len);
        Value read = MESSAGE_PACK.read(bytes);
        list.add(read);
        System.out.println("dedede");
    }
}