package com.edu.JavaLearning.Netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;

import java.net.InetSocketAddress;

/**
 * @Author: tangzh
 * @Date: 2019/3/22$ 7:22 PM$
 **/
public class Netty {

}

class NettyClient{
    public static void main(String[] args) {
        Channel client = new NioSocketChannel();
        ChannelFuture future = client.connect(new InetSocketAddress(8888));
        future.addListener((ChannelFutureListener) future1 -> {
            if(future1.isSuccess()){
                ByteBuf buffer = Unpooled.copiedBuffer("0101,收到请回答".getBytes());
                ChannelFuture wf = future1.channel().write(buffer);
            }else{
                Throwable cause = future1.cause();
                cause.printStackTrace();
            }
        });
    }
}
/**
 * 吃瓜的我copy的
 */
class EchoServer{
    private final int port;
    EchoServer(int port){
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println(
                    "Usage: " + EchoServer.class.getSimpleName() +
                            " ");
        }
        int port = Integer.parseInt(args[0]);
        new EchoServer(port).start();
    }
    public void start() throws Exception{
        final EchoServerHandler serverHandler = new EchoServerHandler();
        //➊ 创建Event-LoopGroup
        NioEventLoopGroup loopGroup = new NioEventLoopGroup();
        try{
            //❷ 创建Server-Bootstrap
            ServerBootstrap b = new ServerBootstrap();
            b.group(loopGroup)
                    .channel(NioServerSocketChannel.class)//❸ 指定所使用的NIO传输Channel
                    .localAddress(new InetSocketAddress(port))//❹ 使用指定的端口设置套接字地址
                    //❺添加一个EchoServer-Handler到子Channel的ChannelPipeline
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(serverHandler);// EchoServerHandler被标注为@Shareable，所以我们可以总是使用同样的实例
                        }
                    });
            //❻ 异步地绑定服务器；调用sync()方法阻塞等待直到绑定完成
            ChannelFuture future = b.bind().sync();
            //❼ 获取Channel的CloseFuture，并且阻塞当前线程直到它完成
            future.channel().closeFuture().sync();
        }finally {
            //❽ 关闭EventLoopGroup，释放所有的资源
            loopGroup.shutdownGracefully().sync();
        }

    }

    /**
     * 逻辑处理类
     */
    class EchoServerHandler extends ChannelInboundHandlerAdapter{

    }
}


/**
 * 还是吃瓜的我copy的
 */
class HttpServer{
    private int port;
    public HttpServer(int port) {
        this.port = port;
    }
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println(
                    "Usage: " + HttpServer.class.getSimpleName() +
                            " <port>");
            return;
        }
        int port = Integer.parseInt(args[0]);
        new HttpServer(port).start();
    }
    public void start() throws Exception {
        ServerBootstrap bootstrap = new ServerBootstrap();
        NioEventLoopGroup loopGroup = new NioEventLoopGroup();
        bootstrap.group(loopGroup)
                 .channel(NioServerSocketChannel.class)
                 .childHandler(new ChannelInitializer<SocketChannel>() {
                     @Override
                     protected void initChannel(SocketChannel ch) throws Exception {
                         ch.pipeline()
                                 .addLast("decoder",new HttpRequestDecoder())
                                 .addLast("encoder",new HttpRequestEncoder())
                                 .addLast("aggregator",new HttpObjectAggregator(512*1024))
                                 .addLast("handler",new HttpHandler());

                     }
                 })
                 .option(ChannelOption.SO_BACKLOG,128)
                 .childOption(ChannelOption.SO_KEEPALIVE,true);
        bootstrap.bind(port).sync();
    }
    class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest>{

        private AsciiString contentType = HttpHeaderValues.TEXT_PLAIN;

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
            System.out.println("class:" + msg.getClass().getName());
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    Unpooled.wrappedBuffer("test".getBytes())); // 2

            HttpHeaders heads = response.headers();
            heads.add(HttpHeaderNames.CONTENT_TYPE, contentType + "; charset=UTF-8");
            heads.add(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes()); // 3
            heads.add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

            ctx.write(response);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            System.out.println("channelReadComplete");
            super.channelReadComplete(ctx);
            ctx.flush(); // 4
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            System.out.println("exceptionCaught");
            if(null != cause) cause.printStackTrace();
            if(null != ctx) ctx.close();
        }
    }
}
