package com.edu.JavaLearning.io.nio;


import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * @Author: tangzh
 * @Date: 2019/3/21$ 10:01 AM$
 **/
public class NIO {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        SocketChannel socketChannel1 = SocketChannel.open();
        SocketChannel socketChannel2 = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1",9999));
        socketChannel1.connect(new InetSocketAddress("127.0.0.1",9999));
        socketChannel2.connect(new InetSocketAddress("127.0.0.1",9999));
        ByteBuffer byteBuffer = ByteBuffer.wrap("服务端你好,我是Client".getBytes());
        ByteBuffer byteBuffer1 = ByteBuffer.wrap("服务端你好,我是Client1".getBytes());
        ByteBuffer byteBuffer2 = ByteBuffer.wrap("服务端你好,我是Client2".getBytes());
        new Thread(new SocketClient(socketChannel,byteBuffer)).start();
        new Thread(new SocketClient(socketChannel1,byteBuffer1)).start();
        new Thread(new SocketClient(socketChannel2,byteBuffer2)).start();
    }

}

/**
 * SocketClient
 * 功能:往通道里写入数据并读取返回数据
 */
class SocketClient implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);

    SocketChannel socketChannel;
    ByteBuffer buffer;
    SocketClient(SocketChannel socketChannel,ByteBuffer buffer){
        this.socketChannel = socketChannel;
        this.buffer = buffer;
    }
    @Override
    public void run() {
        try {
            //将目标数据写入通道
            socketChannel.write(buffer);
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
            int num;
            //将响应数据读出到readBuffer
            while ((num = socketChannel.read(readBuffer)) > 0) {
                readBuffer.flip();
                byte[] re = new byte[num];
                readBuffer.get(re);
                String result = new String(re, Charset.forName("UTF-8"));
                System.out.println("返回值: " + result);
            }
        } catch (IOException e) {
            logger.error("socketClient error",e);
        }finally {
            try {
                socketChannel.close();
            } catch (IOException e) {
                logger.error("socketClient close error",e);
            }
        }
    }
}

/**
 * 异步非阻塞IO 客户端
 */
class AsynchronousSocketClient{
    public static void main(String[] args) throws Exception {
        AsynchronousSocketChannel asySocketChannel = AsynchronousSocketChannel.open();
        Future<?> future = asySocketChannel.connect(new InetSocketAddress("127.0.0.1", 8080));
        future.get();

        Attachment att = new Attachment();
        att.setAsySocketChannel(asySocketChannel);
        att.setReadMode(false);
        att.setBuffer(ByteBuffer.allocate(1024));
        att.getBuffer().put("Hello 我是异步IO客户端".getBytes());
        att.getBuffer().flip();

        //异步发送数据到服务器即数据写入通道
        asySocketChannel.write(att.getBuffer(), att, new CompletionHandler<Integer, Attachment>() {
            @Override
            public void completed(Integer result, Attachment attachment) {
                ByteBuffer buffer = attachment.getBuffer();
                if(attachment.isReadMode){
                    buffer.flip();
                    byte[] bytes = new byte[buffer.limit()];
                    buffer.get(bytes);
                    System.out.println("服务端返回数据:"+new String(bytes));
                    try {
                        attachment.getAsySocketChannel().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    // 写操作完成后，会进到这里
                    attachment.setReadMode(true);
                    buffer.clear();
                    attachment.getAsySocketChannel().read(buffer, attachment, this);
                }
            }

            @Override
            public void failed(Throwable exc, Attachment attachment) {

            }
        });
        // 这里休息一下再退出，给出足够的时间处理数据
        Thread.sleep(2000);
    }
}

/**
 * 异步非阻塞IO服务端(AIO)
 * 异步IO 主要是为了控制线程数量，减少过多的线程带来的内存消耗和 CPU 在线程调度上的开销。
 * 通常，我们会有一个线程池用于执行异步任务，提交任务的线程将任务提交到线程池就可以立马返回，不必等到任务真正完成。
 * 如果想要知道任务的执行结果，通常是通过传递一个回调函数的方式，任务结束后去调用这个函数。
 */
class AsynchronousSocketServer{

    public static void main(String[] args) throws IOException {
        //创建AsySocketServer实例
        AsynchronousServerSocketChannel asynchronousServerSocketChannel =
                AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(8080));
        Attachment att = new Attachment();
        att.setAsySocketServerChannel(asynchronousServerSocketChannel);
        //此处就是线程池执行逻辑,并提供回调函数
        //***SocketServerChannel.accept(...) 获取客户端连接
        asynchronousServerSocketChannel.accept(att, new CompletionHandler<AsynchronousSocketChannel, Attachment>() {
            @Override
            public void completed(AsynchronousSocketChannel client, Attachment attachment) {
                try {
                    //主要逻辑为107行
                    SocketAddress clientAdress = client.getRemoteAddress();
                    System.out.println("收到"+clientAdress+"的连接");
                    attachment.getAsySocketServerChannel().accept(attachment,this);

                    Attachment newAtt = new Attachment();
                    newAtt.setAsySocketServerChannel(asynchronousServerSocketChannel);
                    newAtt.setAsySocketChannel(client);
                    newAtt.setReadMode(true);
                    newAtt.setBuffer(ByteBuffer.allocate(1024));

                    client.read(newAtt.getBuffer(), newAtt, new CompletionHandler<Integer, Attachment>() {
                        @Override
                        public void completed(Integer result, Attachment attachment) {
                            if(attachment.isReadMode()){
                                //获取客户端数据and todoSomething
                                ByteBuffer buffer = attachment.getBuffer();
                                buffer.flip();
                                byte[] bytes = new byte[buffer.limit()];
                                buffer.get(bytes);
                                System.out.println("客户端:"+new String(bytes).trim());

                                //返回数据给客户端
                                buffer.clear();
                                buffer.put("小子,异步IO服务端收到你的消息了".getBytes());
                                attachment.setReadMode(false);
                                buffer.flip();
                                attachment.getAsySocketChannel().write(buffer);
                            }
                        }

                        @Override
                        public void failed(Throwable exc, Attachment attachment) {
                            System.out.println("连接断开");
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable exc, Attachment attachment) {
                System.out.println("连接断开");
            }
        });
        // 为了防止 main 线程退出
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
        }
    }

}


/**
 * AsySocketServerChannel 消息传递类
 */
class Attachment {
    //服务端
    AsynchronousServerSocketChannel asySocketServerChannel;
    //客户端
    AsynchronousSocketChannel asySocketChannel;
    //数据
    ByteBuffer buffer;
    boolean isReadMode;

    public boolean isReadMode() {
        return isReadMode;
    }

    public void setReadMode(boolean readMode) {
        isReadMode = readMode;
    }

    public AsynchronousServerSocketChannel getAsySocketServerChannel() {
        return asySocketServerChannel;
    }

    public void setAsySocketServerChannel(AsynchronousServerSocketChannel asySocketServerChannel) {
        this.asySocketServerChannel = asySocketServerChannel;
    }

    public AsynchronousSocketChannel getAsySocketChannel() {
        return asySocketChannel;
    }

    public void setAsySocketChannel(AsynchronousSocketChannel asySocketChannel) {
        this.asySocketChannel = asySocketChannel;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public void setBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }

}


/**
 * 同步非阻塞型IO + Selector(NIO)
 */
class SelectorSocketServer{

    private static Attachment attachment;

    public static void main(String[] args) throws IOException {
        //获取selector实例
        Selector selector = Selector.open();
        //创建serverSocketChannel并监听8080端口
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(9999));
        //将其注册到Selector中,设置非阻塞,监听OP_ACCEPT事件
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true){
            if(selector.select()==0){
                continue;
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()){
                SelectionKey key = keyIterator.next();
                keyIterator.remove();
                if(key.isAcceptable()){
                    // 有已经接受的新的到服务端的连接
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    // 有新的连接并不代表这个通道就有数据，
                    // 这里将这个新的 SocketChannel 注册到 Selector，监听 OP_READ 事件，等待数据
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ,attachment);
                }else if(key.isReadable()){
                    System.out.println(key.attachment());
                    SocketChannel channel = (SocketChannel)key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int read = channel.read(buffer);
                    if(read>0){
                        String getData = new String(buffer.array()).trim();
                        System.out.println("收到数据:"+getData);
                        ByteBuffer wrap = ByteBuffer.wrap(("小子你的数据我收到了").getBytes());
                        channel.write(wrap);
                    }else if(read==-1){
                        channel.close();
                    }
                }
            }
        }

    }
}

/**
 * 同步阻塞型IO服务端(BIO)
 * 阻塞型IO,一个连接一个线程
 * 那么，这个模式下的性能瓶颈在哪里呢？
 * 首先，每次来一个连接都开一个新的线程这肯定是不合适的。
 *      当活跃连接数在几十几百的时候当然是可以这样做的，但如果活跃连接数是几万几十万的时候，这么多线程明显就不行了。
 *      每个线程都需要一部分内存，内存会被迅速消耗，同时，线程切换的开销非常大。
 * 其次，阻塞操作在这里也是一个问题。
 *      首先，accept() 是一个阻塞操作，当 accept() 返回的时候，代表有一个连接可以使用了，
 *      我们这里是马上就新建线程来处理这个 SocketChannel 了，但是，但是这里不代表对方就将数据传输过来了。
 *      所以，SocketChannel#read 方法将阻塞，等待数据，明显这个等待是不值得的。同理，write 方法也需要等待通道可写才能执行写入操作，这边的阻塞等待也是不值得的。
 */
class SocketServer{

    public static void main(String[] args) throws IOException {
        //实例化ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //监听8080端口
        serverSocketChannel.socket().bind(new InetSocketAddress(8080));
        while (true){
            //获取客户端Socket连接
            SocketChannel socketChannel = serverSocketChannel.accept();
            //新建一个线程处理Socket连接
            SocketHandler socketHandler = new SocketHandler(socketChannel);
            new Thread(socketHandler).start();
        }
    }
}

/**
 * 阻塞型IO,SocketHandler。处理具体事务逻辑的线程
 */
class SocketHandler implements Runnable{

    SocketChannel socketChannel;
    SocketHandler(SocketChannel socketChannel){
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
        //创建大小为1024的buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            int num;
            //从channel中获取数据流到buffer
            while ((num = socketChannel.read(buffer))>0){
                //切换模式 写入->读出
                buffer.flip();
                byte[] bytes = new byte[num];
                //buffer的数据写入bytes中
                buffer.get(bytes);
                String data = new String(bytes, Charset.forName("UTF-8"));
                //至此获取到了Socket请求的二进制数据  doService
                System.out.println("接收数据："+data);
                //==============数据返回===============//
                //创建数据为data,大小为data.length的buffer
                ByteBuffer wrap = ByteBuffer.wrap(("服务端接收到的数据为:" + data).getBytes());
                socketChannel.write(wrap);
                buffer.clear();
            }
        } catch (IOException e) {
            IOUtils.closeQuietly(socketChannel);
        }
    }
}
