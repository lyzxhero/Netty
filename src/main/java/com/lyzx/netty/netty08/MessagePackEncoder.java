package com.lyzx.netty.netty08;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * 使用
 */
public class MessagePackEncoder extends MessageToByteEncoder {
    private static final MessagePack pack = new MessagePack();


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        byte[] bytes = pack.write(o);
        byteBuf.writeBytes(bytes);
    }
}
