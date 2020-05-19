package com.edu.JavaLearning.io.chat.common;


import com.edu.common.result.ResultData;

import java.nio.ByteBuffer;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/18 2:18 PM
 **/
public class RemoteCommand {

    private int code;
    private Message message;
    private ResultData resultData;


    public static RemoteCommand createRemoteCommand(int code, Message message) {
        RemoteCommand command = new RemoteCommand();
        command.setCode(code);
        command.setMessage(message);
        return command;
    }

    public static RemoteCommand createRespRemoteCommand(int code,ResultData resultData){
        RemoteCommand command = new RemoteCommand();
        command.setResultData(resultData);
        command.setCode(code);
        return command;
    }


    public ByteBuffer encodeByCode(boolean isReq) {
        int length = 8;

        byte[] body = isReq ? AbstractRemoteSerializable.encode(message) : AbstractRemoteSerializable.encode(resultData);
        if (body == null) {
            throw new IllegalArgumentException("数据不可为空");
        }
        length += body.length;

        ByteBuffer buffer = ByteBuffer.allocate(length);

        //length
        buffer.putInt(length);
        //code
        buffer.putInt(code);
        //body
        buffer.put(body);
        buffer.flip();

        return buffer;
    }

    public static RemoteCommand decodeByCode(ByteBuffer in) {
        RemoteCommand command = new RemoteCommand();

        int length = in.getInt();
        if (length < 4) {
            return null;
        }
        int code = in.getInt();
        byte[] body = new byte[length - 8];
        in.get(body, 0, length - 8);
        command.setCode(code);
        switch (code) {
            case 1:
                command.setMessage(AbstractRemoteSerializable.decode(body, QueryChatListRequest.class));
                break;
            case 2:
                command.setResultData(AbstractRemoteSerializable.decode(body,ResultData.class));
        }
        return command;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public ResultData getResultData() {
        return resultData;
    }

    public void setResultData(ResultData resultData) {
        this.resultData = resultData;
    }

    @Override
    public String toString() {
        return "RemoteCommand{" +
                "code=" + code +
                ", message=" + message +
                ", resultData=" + resultData +
                '}';
    }
}
