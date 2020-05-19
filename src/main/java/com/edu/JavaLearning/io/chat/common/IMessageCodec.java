package com.edu.JavaLearning.io.chat.common;

import java.nio.ByteBuffer;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/18 5:10 PM
 **/
public interface IMessageCodec {

    /**
     * 消息解析 buf -> message
     *
     * @param buf
     * @return
     */
    Object decode(ByteBuffer buf);

    /**
     * message -> buf
     *
     * @param message
     * @return
     */
    ByteBuffer encode(Object message);
}
