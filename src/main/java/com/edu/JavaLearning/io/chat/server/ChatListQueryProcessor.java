package com.edu.JavaLearning.io.chat.server;

import com.edu.JavaLearning.io.chat.common.QueryChatListRequest;
import com.edu.JavaLearning.io.chat.common.RemoteCommand;
import com.edu.common.result.ResultData;
import com.google.common.collect.Lists;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/18 9:57 PM
 **/
@Component
public class ChatListQueryProcessor extends AbstractProcessor{

    private static int code = 1;

    public void execute(ChannelHandlerContext ctx, QueryChatListRequest request) {
        String chatId = request.getChatId();
        String memberId = request.getMemberId();

        Runnable task = () -> {
            List<String> result = doDbQuery(chatId, memberId);
            RemoteCommand respRemoteCommand = RemoteCommand.createRespRemoteCommand(2, ResultData.defaultSuccess(result));
            ctx.writeAndFlush(respRemoteCommand);
        };
        CHAT_QUERY_EXECUTOR.submit(task);
    }

    public List<String> doDbQuery(String chatId, String memberId) {
        return Lists.newArrayList(chatId + memberId);
    }

    @PostConstruct
    public void init(){
        processorMap.put(code,this);
    }
}
