package com.edu.JavaLearning.io.chat;

import com.edu.JavaLearning.io.chat.common.MessageDecoder;
import com.edu.JavaLearning.io.chat.common.MessageServerEncoder;
import com.edu.JavaLearning.io.chat.common.QueryChatListRequest;
import com.edu.JavaLearning.io.chat.common.RemoteCommand;
import com.edu.JavaLearning.io.chat.proto.MessageProtobuf;
import com.edu.JavaLearning.io.chat.server.ChatListQueryProcessor;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/18 1:43 PM
 * netty tcp 长连接消息服务端
 **/
public class TcpChatServer {

    private final static Logger log = LoggerFactory.getLogger(TcpChatServer.class);

    private static String OS_NAME = System.getProperty("os.name");
    private EventLoopGroup boss;
    private EventLoopGroup worker;
    private ChannelFuture future;
    private int port = 9999;

    public static void main(String[] args) {
        new TcpChatServer().start();
    }


    public synchronized void start() {
        try {
            openServer(port);
        } catch (Exception e) {
            log.error("open TcpChatServer fail", e);
        }
    }

    private void openServer(int port) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        int nCpu = Runtime.getRuntime().availableProcessors();
        if(useEpool()){
            boss = new EpollEventLoopGroup(1, new ThreadFactory() {
                private AtomicInteger threadIndex = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread("TcpChatBossEPoolThread-" + this.threadIndex.incrementAndGet());
                }
            });
        } else {
            boss = new NioEventLoopGroup(1, new ThreadFactory() {
                private AtomicInteger threadIndex = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread("TcpChatBossNIOThread-" + this.threadIndex.incrementAndGet());
                }
            });
        }
        boss = useEpool() ? new EpollEventLoopGroup(1) : new NioEventLoopGroup(1);
        worker = useEpool() ? new EpollEventLoopGroup(nCpu) : new NioEventLoopGroup(nCpu);
        bootstrap.group(boss, worker)
                .channel(useEpool() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();

                        //netty自定义长度解码器,解决Tcp拆包/粘包问题
                        pipeline.addLast("frameEncoder",new LengthFieldPrepender(2));
                        pipeline.addLast("frameDecoder",new LengthFieldBasedFrameDecoder(65535,0,2,0,2));

                        //protoBuf 编码
                        pipeline.addLast(new ProtobufEncoder());
                        pipeline.addLast(new ProtobufDecoder(MessageProtobuf.Msg.getDefaultInstance()));

//                        pipeline.addLast(new MessageDecoder());
//                        pipeline.addLast(new MessageServerEncoder());
                        pipeline.addLast(new IdleStateHandler(0, 0, 10));
                        pipeline.addLast(new NettyServerConnectManager());
                        pipeline.addLast(new NettyServerHandler());
                    }
                });
        try {
            future = bootstrap.bind(port).sync();
            log.info("TcpChatServer started");
        } catch (Exception e) {
            log.error("TcpChatServer start fail", e);
        }
    }

    private boolean useEpool() {
        return StringUtils.isNotBlank(OS_NAME) && OS_NAME.toLowerCase().contains("linux") && Epoll.isAvailable();
    }

    class NettyServerHandler extends SimpleChannelInboundHandler<MessageProtobuf.Msg> {

//        @Override
//        protected void channelRead0(ChannelHandlerContext ctx, RemoteCommand msg) throws Exception {
//            processRemoteCommand(ctx, msg);
//        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, MessageProtobuf.Msg msg) throws Exception {
            String chatId = msg.getHead().getChatId();
            System.out.println(chatId);
            MessageProtobuf.Msg.Builder msgBuilder = MessageProtobuf.Msg.newBuilder();
            MessageProtobuf.Head.Builder headBuilder = MessageProtobuf.Head.newBuilder();
            MessageProtobuf.Msg resp = msgBuilder.setHead(headBuilder.setMsgId("哈哈哈").build()).build();
            ctx.writeAndFlush(resp);
        }
    }

    class NettyServerConnectManager extends ChannelDuplexHandler{
        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            log.info("NETTY SERVER PIPELINE: channelRegistered {}",ctx.channel().remoteAddress().toString());
            super.channelRegistered(ctx);
        }

        @Override
        public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
            log.info("netty connect:"+remoteAddress.toString());
            super.connect(ctx, remoteAddress, localAddress, promise);
        }
    }

    public void processRemoteCommand(ChannelHandlerContext ctx, RemoteCommand msg) {
        int code = msg.getCode();
        switch (code) {
            case 1:
                new ChatListQueryProcessor().execute(ctx, (QueryChatListRequest) msg.getMessage());
                break;
        }
    }
}
