package com.edu.JavaLearning.Netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * @Author: tangzh
 * @Date: 2019/7/10$ 8:22 PM$
 **/
public class NettyForHttp {

    public static void main(String[] args) throws InterruptedException {
        //acceptGroup是获取连接的，workerGroup是用来处理连接的，这二个线程组都是死循环
        NioEventLoopGroup acceptGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //简化服务端启动的一个类
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(acceptGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new HttpServerChannelInitializer());

            System.out.println("================服务端已启动,端口监听:9999============");
            ChannelFuture channelFuture = bootstrap.bind(9999).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            acceptGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}

class HttpServerChannelInitializer extends ChannelInitializer {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //http协议的编解码使用的,是HttpRequestDecoder和HttpResponseEncoder处理器组合
        //HttpRequestDecoder http请求的解码
        //HttpResponseEncoder http请求的编码
        pipeline.addLast("httpServerCodec",new HttpServerCodec());
        pipeline.addLast("httpServerHandler",new HttpServerHandler());
    }
}


/**
 * 基于事件驱动,不同事件会触发不同方法的回调
 */
class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject>{

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if(msg instanceof HttpRequest){
            HttpRequest request = (HttpRequest)msg;
            System.out.println("请求方法:"+request.method().name());

            URI uri = new URI(request.uri());
            //使用浏览器访问127.0.0.1:9999会发送二次请求，其中有一次是127.0.0.1:9999/favicon.ico,这个url请求访问网站的图标
            if("/favicon.ico".equals(uri.getPath())){
                System.out.println("请求favicon.ico");
                return;
            }
            //向客户端返回的内容
            ByteBuf content = Unpooled.copiedBuffer("小伙子,咋肥四", CharsetUtil.UTF_8);
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,content);

            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"application/json");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());

            ctx.writeAndFlush(response);
            //其实更合理的close连接应该判断是http1.O还是1.1来进行判断请求超时时间来断开channel连接。
            ctx.channel().close();
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel Registered");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel UnRegistered");
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel Active");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel Inactive");
        super.channelInactive(ctx);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel Added");
        super.handlerAdded(ctx);
    }
}
