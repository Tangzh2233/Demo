package com.edu.JavaLearning.IO;


import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * @author Tangzhihao
 * @date 2018/4/27
 */

public class IO {
    public static void main(String[] args) throws IOException {
//        BaseInPutStreamOfByteArray();
    //    BaseInPutStreamOfStringBuffer();
        BaseInPutStreamOfFile();
    //    BaseRandomAccessFile();
    //    NioOfCaseDoCopy();
//        NioOfCaseDoRead();
    }


    public static void BaseInPutStreamOfByteArray() throws IOException {
        byte[] bytes = new byte[]{1,0,0,1,2,0};
        byte[] context = new byte[bytes.length];
        //byte数组作为数据源,读入流中
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        int num;
        bais.read(context);
        while ((num = bais.read())!=-1){
            System.out.println(num);
        }
        bais.close();
    }
    public static void BaseInPutStreamOfStringBuffer() throws IOException {
        String str = "蒙多,春哥是个大傻子!";
        StringReader sr = new StringReader(str);
        char[] chars = new char[1024];
        sr.read(chars);
        sr.close();
        System.out.println("bytes"+new String(chars));
    }
    public static void BaseInPutStreamOfFile() throws IOException {
        File file = new File("/data/GZip.txt");
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        FileOutputStream fos = new FileOutputStream(new File("/data/GZip-1.txt"),true);
        byte[] bytes = new byte[1024];
        int read = bis.read(bytes);
        fos.write(bytes,0,read);
        bis.close();
        fis.close();
        fos.flush();
        System.out.println(new String(bytes,Charset.forName("UTF-8")));
    }

    public static void BaseRandomAccessFile() throws IOException {
        File file = new File("D:\\GZip.txt");
        //设置连接文件以及对文件的读写权限
        RandomAccessFile raf = new RandomAccessFile(file,"rw");
        raf.seek(raf.length());
        raf.write("追加内容".getBytes());
        /*String s;
        while ((s = raf.readLine())!=null){
            System.out.println(s);
        }*/

    }

    public static void NioOfCaseDoRead() throws IOException {
        File file = new File("D:\\GZip.txt");
        FileInputStream fis = new FileInputStream(file);
        FileChannel fc = fis.getChannel();
        ByteBuffer bf = ByteBuffer.allocate(1024);
        while (true){
            bf.clear();
            int read = fc.read(bf);
            if(read==-1){break;}
            bf.flip();
            System.out.println(read);
        }
        fc.close();
        fis.close();
    }

    public static void NioOfCaseDoCopy() throws IOException {
        File file = new File("D:\\GZip.txt");
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream("D:\\GZip-1.txt",true);
        //获取读通道
        FileChannel fcin = fis.getChannel();
        //获取写通道
        FileChannel fcon = fos.getChannel();
        //定义缓冲区
        ByteBuffer bbf = ByteBuffer.allocate(1024);
        while(true){
            //清空缓冲区
            bbf.clear();
            //读取一个字节
            int read = fcin.read(bbf);
            if(read==-1){
                break;
            }
            //将缓冲区指针指向头部
            bbf.flip();
            //从缓冲区向文件写入数据
            fcon.write(bbf);
        }
        System.out.println("执行完成!");
        fcon.close();
        fcin.close();
        fos.flush();
        fis.close();

    }
}
