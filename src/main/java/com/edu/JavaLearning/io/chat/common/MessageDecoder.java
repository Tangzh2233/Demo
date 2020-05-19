package com.edu.JavaLearning.io.chat.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteBuffer;
/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/18 2:18 PM
 * tcp 数据解析 byte -> object
 **/
public class MessageDecoder extends LengthFieldBasedFrameDecoder {


    public MessageDecoder(){
        super(16777216,0,4,-4,0);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = null;
        try {
            frame = (ByteBuf) super.decode(ctx, in);
            if (null == frame) {
                return null;
            }

            ByteBuffer byteBuffer = frame.nioBuffer();

            return RemoteCommand.decodeByCode(byteBuffer);
        } catch (Exception e) {
        } finally {
            if (null != frame) {
                frame.release();
            }
        }

        return null;
    }
}
