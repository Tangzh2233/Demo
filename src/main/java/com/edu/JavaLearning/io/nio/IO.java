package com.edu.JavaLearning.io.nio;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/29 10:01 AM
 **/
public class IO {


    public static void main(String[] args) {

    }


    /**
     * 阻塞型即BIO
     * 此处的阻塞在accept
     */
    static class BIOServer {

        public static void main(String[] args) throws IOException {
            ServerSocket server = new ServerSocket();
            server.bind(new InetSocketAddress(9090));
            System.out.println("阻塞BIO Server started");

            while (true) {
                Socket accept = server.accept();

                new Thread(() -> {
                    try {
                        readAndWrite(accept);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }


    private static void readAndWrite(Socket accept) throws IOException {
        byte[] bytes = new byte[20];
        System.out.println("获取客户端链接 port: " + accept.getPort() + "等待数据读取....");
        BufferedInputStream inputStream = new BufferedInputStream(accept.getInputStream());
        inputStream.read(bytes);
        String message = new String(bytes, Charset.forName("UTF-8"));
        System.out.println(message);

        //数据回写client
        DataOutputStream outputStream = new DataOutputStream(accept.getOutputStream());
        outputStream.writeChars("Server: " + message);
        inputStream.close();
    }
}
