package com.edu.JavaLearning.io.chat.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.ByteBuffer;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/18 2:14 PM
 * 数据解析  object -> tcp
 **/
public class MessageServerEncoder extends MessageToByteEncoder<RemoteCommand> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RemoteCommand msg, ByteBuf out) throws Exception {
        ByteBuffer encode = msg.encodeByCode(false);
        out.writeBytes(encode);
    }
}
