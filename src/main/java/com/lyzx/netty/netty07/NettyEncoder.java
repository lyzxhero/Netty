package com.lyzx.netty.netty07;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class NettyEncoder extends MessageToByteEncoder<NettyRequest> {

    @Override
    protected void encode(ChannelHandlerContext ctx, NettyRequest nettyRequest, ByteBuf byteBuf) throws Exception {

    }
}
