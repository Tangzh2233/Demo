package com.edu.JavaLearning.io.chat;

import com.edu.JavaLearning.io.chat.common.MessageClientEncoder;
import com.edu.JavaLearning.io.chat.common.MessageDecoder;
import com.edu.JavaLearning.io.chat.common.QueryChatListRequest;
import com.edu.JavaLearning.io.chat.common.RemoteCommand;
import com.edu.JavaLearning.io.chat.proto.MessageProtobuf;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/18 1:45 PM
 * netty tcp 长连接消息客户端
 **/
public class TcpChatClient {

    private final static Logger log = LoggerFactory.getLogger(TcpChatServer.class);

    private static String OS_NAME = System.getProperty("os.name");
    private EventLoopGroup boss;
    private Bootstrap client;

    public static void main(String[] args) {
        TcpChatClient client = new TcpChatClient();
        client.init();
        ChannelFuture connect = client.client.connect(new InetSocketAddress("127.0.0.1", 9999));
        connect.awaitUninterruptibly(100, TimeUnit.MILLISECONDS);
        if(!connect.isSuccess()){
            return;
        }
//        QueryChatListRequest request = new QueryChatListRequest();
//        request.setMemberId("999655454931939329");
//        request.setChatId("G1109530606053617715");
//        RemoteCommand.createRemoteCommand(1,request)
        MessageProtobuf.Msg.Builder msgBuilder = MessageProtobuf.Msg.newBuilder();
        MessageProtobuf.Head.Builder headBuilder = MessageProtobuf.Head.newBuilder();
        MessageProtobuf.Msg request = msgBuilder.setHead(headBuilder.setChatId("G1109530606053617715").build()).build();

        Channel channel = connect.channel();
        channel.writeAndFlush(request);
        System.out.println(channel.isActive());

    }

    public synchronized void init(){
        openClient();
    }


    public void openClient(){
        Bootstrap bootstrap = new Bootstrap();
        boss = new NioEventLoopGroup(1);
        bootstrap.group(boss).channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        //netty自定义长度解码器,解决Tcp拆包/粘包问题
                        pipeline.addLast("frameEncoder",new LengthFieldPrepender(2));
                        pipeline.addLast("frameDecoder",new LengthFieldBasedFrameDecoder(65535,0,2,0,2));

                        //protoBuf 编码
                        pipeline.addLast(new ProtobufEncoder());
                        pipeline.addLast(new ProtobufDecoder(MessageProtobuf.Msg.getDefaultInstance()));

                        //自定义解码器
//                        pipeline.addLast(new MessageDecoder());
//                        pipeline.addLast(new MessageClientEncoder());

                        pipeline.addLast(new IdleStateHandler(0, 0, 10));
                        pipeline.addLast(new NettyClientHandler());
                    }
                });
        client = bootstrap;
        try {
            log.info("TcpChatClient started");
        } catch (Exception e) {
            log.error("TcpChatClient start fail", e);
        }
    }

    private boolean useEpool() {
        return StringUtils.isNotBlank(OS_NAME) && OS_NAME.toLowerCase().contains("linux") && Epoll.isAvailable();
    }

    class NettyClientHandler extends SimpleChannelInboundHandler<MessageProtobuf.Msg> {

//        @Override
//        protected void channelRead0(ChannelHandlerContext ctx, RemoteCommand msg) throws Exception {
//            System.out.println(msg.toString());
////            processRemoteCommand(ctx, msg);
//        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, MessageProtobuf.Msg msg) throws Exception {
            System.out.println(msg.getHead().getMsgId());
        }
    }

    class NettyClientConnectManager extends ChannelDuplexHandler{
        @Override
        public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
            log.info("netty client connect to: "+ remoteAddress.toString());
            super.connect(ctx, remoteAddress, localAddress, promise);
        }
    }
}
