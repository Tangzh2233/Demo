package com.edu.JavaLearning.Netty.longpolling;

import com.edu.util.RedisUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.mail.MethodNotSupportedException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/3 10:57 PM
 * 简单实现长轮询的服务端
 **/
public class HttpLongPollingServer {

    private static EventExecutorGroup eventExecutors = new DefaultEventExecutorGroup(12);
    private static BlockingQueue<String> dataQueue = new ArrayBlockingQueue<>(1000);
    private static String osName = System.getProperty("os.name");
    private static ConcurrentHashMap<String, HashMap<String, HolderHttpRequest>> holdMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, String> dataMap = new ConcurrentHashMap<>();

    private EventLoopGroup boss;
    private EventLoopGroup worker;
    private ChannelFuture future;

    public static void main(String[] args) {
        dataQueue.offer("我");
        dataQueue.offer("是");
        dataQueue.offer("服");
        dataQueue.offer("务");
        dataQueue.offer("端");
        new HttpLongPollingServer().serverStart();
    }

    public synchronized void serverStart() {
        //cpu个数
        int nCpu = Runtime.getRuntime().availableProcessors();
        boss = useEpoll() ? new EpollEventLoopGroup(1) : useKqueue() ? new KQueueEventLoopGroup(1) : new NioEventLoopGroup(1);
        worker = useEpoll() ? new EpollEventLoopGroup(nCpu) : useKqueue() ? new KQueueEventLoopGroup(1) : new NioEventLoopGroup(nCpu);

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.channel(useEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .group(boss, worker)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast("http-decoder", new HttpServerCodec());
                        socketChannel.pipeline().addLast(eventExecutors, new HttpServerHandler());
                    }
                });
        try {
            future = serverBootstrap.bind(9090).sync();
            System.out.println("========HttpServer init=========");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void destroy() {
        try {
            future.channel().closeFuture();
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isLinuxPlatform() {
        return StringUtils.isNotBlank(osName) && (osName.toLowerCase().contains("linux"));
    }

    private boolean isMacPlatform() {
        return StringUtils.isNotBlank(osName) && (osName.toLowerCase().contains("mac"));
    }

    private boolean useEpoll() {
        return isLinuxPlatform() && Epoll.isAvailable();
    }

    private boolean useKqueue() {
        return isMacPlatform() && KQueue.isAvailable();
    }

    /**
     * 读取数据和返回
     */
    static class HttpServerHandler extends SimpleChannelInboundHandler<TcpRequest> {

        private static ConcurrentHashMap<String, HolderHttpRequest> hold = new ConcurrentHashMap<>();

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, TcpRequest tcpRequest) throws Exception {

            HolderHttpRequest request = hold.putIfAbsent(tcpRequest.getReqKey(), new HolderHttpRequest(channelHandlerContext, tcpRequest));
        }

        public void handlerRequest(ChannelHandlerContext ctx, HttpRequest request) throws IOException, MethodNotSupportedException {
            Map<String, String> parse = HttpParamParse.parse(request);
            String chatId = parse.get("chatId");
            String memberId = parse.get("memberId");
            String lastId = parse.get("lastId");
            List<String> dataList = RedisUtil.lrange(chatId + memberId, Integer.valueOf(lastId));
            if (dataList.size() > 0) {
                ctx.writeAndFlush(dataList);
            }
        }
    }

    @Data
    static class HolderHttpRequest {
        private ChannelHandlerContext context;
        private TcpRequest request;

        public HolderHttpRequest(ChannelHandlerContext context, TcpRequest request) {
            this.context = context;
            this.request = request;
        }

        public ChannelHandlerContext getContext() {
            return context;
        }

        public void setContext(ChannelHandlerContext context) {
            this.context = context;
        }

        public TcpRequest getRequest() {
            return request;
        }

        public void setRequest(TcpRequest request) {
            this.request = request;
        }
    }

    class checTask implements Runnable {

        @Override
        public void run() {
        }
    }

}



