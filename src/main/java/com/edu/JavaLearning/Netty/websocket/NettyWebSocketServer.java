package com.edu.JavaLearning.Netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/11 3:35 PM
 **/
public class NettyWebSocketServer {

    private static String osName = System.getProperty("os.name");
    private static EventExecutorGroup group = new DefaultEventExecutorGroup(12);

    private EventLoopGroup boss = null;
    private EventLoopGroup worker = null;

    public synchronized void start() {

        int nCpu = Runtime.getRuntime().availableProcessors();

        boss = useEpoll() ? new EpollEventLoopGroup(nCpu) : new NioEventLoopGroup(nCpu);
        worker = useEpoll() ? new EpollEventLoopGroup(nCpu) : new NioEventLoopGroup(nCpu);

        try {
            ServerBootstrap server = new ServerBootstrap();
            server.channel(useEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                    .group(boss, worker)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast("http-codec", new HttpServerCodec());
                            pipeline.addLast("ping", new IdleStateHandler(2, 1, 3, TimeUnit.SECONDS));
                            pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
                            pipeline.addLast(group, "handle", new WebSocketHandler());
                        }
                    });
            server.bind(9090).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
        }

    }


    private boolean isLinuxPlatform() {
        return StringUtils.isNotBlank(osName) && (osName.toLowerCase().contains("linux"));
    }

    private boolean useEpoll() {
        return isLinuxPlatform() && Epoll.isAvailable();
    }


    class WebSocketHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {

            super.channelActive(ctx);
        }
    }
}
