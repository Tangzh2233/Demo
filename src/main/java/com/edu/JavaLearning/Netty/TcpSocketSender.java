package com.edu.JavaLearning.Netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @Author: tangzh
 * @Date: 2019/7/10$ 4:54 PM$
 * 自定义数据发送类,netty
 **/
public class TcpSocketSender {

    private BlockingQueue<String> dataQueue = new ArrayBlockingQueue<>(5000);
    private Bootstrap bootstrap;
    private InetSocketAddress serverAdress;

    public TcpSocketSender(String ip){
        this.serverAdress = parseSocketAddress(ip);
        NioEventLoopGroup group = new NioEventLoopGroup(1, r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE,true);
        bootstrap.handler(new ChannelInitializer() {
            @Override
            protected void initChannel(Channel ch) throws Exception {

            }
        });
        this.bootstrap = bootstrap;
        new Thread(new Worker()).start();
    }

    public boolean send(String data){
        return dataQueue.offer(data);
    }

    private void doSend(){
        do {
            ChannelFuture future = getChannelFuture();
            try {
                if (future != null) {
                    String data = dataQueue.poll();
                    if (data != null) {
                        doSend(future, data);
                    } else {
                        Thread.sleep(5);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (true);
    }

    private ChannelFuture getChannelFuture(){
        return bootstrap.connect(serverAdress);
    }

    private void doSend(ChannelFuture future,String data){
        ByteBuf byteBuf = Unpooled.copiedBuffer(data, CharsetUtil.UTF_8);
        future.channel().writeAndFlush(byteBuf);
    }

    private InetSocketAddress parseSocketAddress(String ip){
        if(StringUtils.isNotBlank(ip)){
            String[] split = ip.split(":");
            return new InetSocketAddress(split[0],Integer.parseInt(split[1]));
        }
        return new InetSocketAddress("127.0.0.1",8080);
    }

    class Worker implements Runnable{

        @Override
        public void run() {
            doSend();
        }
    }
}
