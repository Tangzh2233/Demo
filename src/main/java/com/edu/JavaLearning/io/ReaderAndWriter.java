package com.edu.JavaLearning.io;

import java.io.*;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/17 11:54 AM
 * 字符输入输出流超类
 * @see java.io.Reader,java.io.Writer
 * //面向字符数组的,字符输入输出
 * @see java.io.CharArrayReader,java.io.CharArrayWriter
 * //带有缓冲区的字符数组输入输出。和 BufferedInputStream一个功能
 * @see java.io.BufferedReader,java.io.BufferedWriter
 * //面向文件的字符输入输出流
 * @see java.io.FileReader,java.io.FileWriter
 * //面向字符串的字符输入输出流
 * @see java.io.StringReader,java.io.StringWriter
 * //面向管道的字符输入输出
 * @see java.io.PipedReader,java.io.PipedWriter
 * //跟踪行号的缓冲字符输入流。
 * @see LineNumberReader
 * //将chat[]反过来读入到pushback的内置字符数组中。有取消读取的意思
 * @see java.io.PushbackReader#unread(char[])
 *
 * @see InputStreamReader,OutputStreamWriter  //字节流 <-> 字符流 桥梁
 **/
public class ReaderAndWriter {

    private static int size = 10;

    public static void main(String[] args) throws IOException {
        chatArrayReader();
        fileReader();
        inputStreamReader();
        bufferedReader();
        stringReader();
    }


    /**
     * 字符数组reader or writer
     *
     * @throws IOException
     */
    public static void chatArrayReader() throws IOException {
        char[] data = new char[]{'a', 'c', 'd', 'b', 'x', 'p', 'e', 'l', 'o'};
        CharArrayReader reader = new CharArrayReader(data);
        CharArrayWriter writer = new CharArrayWriter();
        for (int i = 0; i < data.length; i++) {
            int read = reader.read();
            //char字符拼接
            writer.append((char) read);
            //append操作底层一致
//            writer.write((char)read);
            if (read == 'b') {
                //mark就是记录一下当前pos
                reader.mark(i);
            }
            System.out.println((char) read);
        }
        System.out.println(writer.toString());
        System.out.println("======reset=====");
        //reset就是让pos -> mark
        reader.reset();
        int c;
        while ((c = reader.read()) != -1) {
            System.out.println((char) c);
        }
    }

    /**
     * 字符数组读取文件
     *
     * @throws IOException
     */
    public static void fileReader() throws IOException {
        FileReader fileReader = new FileReader("/data/GZip.txt");
        char[] chars = new char[size];
        long begin = System.currentTimeMillis();
        while (fileReader.ready()) {
            fileReader.read(chars);
        }
        System.out.println("Reader: " + (System.currentTimeMillis() - begin) + "ms");
        fileReader.close();
    }

    /**
     * 字符数组缓冲区读取文件
     *
     * @see InputStream
     * @see InputStreamReader
     * @see BufferedReader
     * 字符数组 -> 字符缓冲数组
     */
    public static void bufferedReader() throws IOException {
        FileReader fileReader = new FileReader("/data/GZip.txt");
        BufferedReader reader = new BufferedReader(fileReader);
        char[] chars = new char[size];
        long begin = System.currentTimeMillis();
        while (reader.ready()) {
            reader.read(chars);
        }
        System.out.println("BufferedReader: " + (System.currentTimeMillis() - begin) + "ms");
        reader.close();
        fileReader.close();
    }

    /**
     * 字节数组 -> 字符数组
     *
     * @throws IOException
     */
    public static void inputStreamReader() throws IOException {
        FileInputStream inputStream = new FileInputStream("/data/GZip.txt");
        InputStreamReader streamReader = new InputStreamReader(inputStream);
        char[] chars = new char[size];
        long begin = System.currentTimeMillis();
        while (streamReader.ready()) {
            streamReader.read(chars);
        }
        System.out.println("InputStreamReader: " + (System.currentTimeMillis() - begin) + "ms");
        streamReader.close();
        inputStream.close();
    }

    public static void stringReader() throws IOException {
        String data = "飘牙是个大傻子!";
        StringReader reader = new StringReader(data);
        reader.skip(2);
        for (int i = 0; i < data.length(); i++) {
            int read = reader.read();
            if (read == -1) {
                break;
            }
            if (i == 3) {
                reader.mark(3);
            }
            System.out.println((char) read);
        }
        reader.reset();
        int c;
        //此时的reset回到的是pos==6
        //因为skip = 2,此时pos=2,再当i=3此时循环4次。此时的pos = 2+4 = 6
        while ((c = reader.read()) != -1) {
            System.out.println((char) c);
        }
        reader.close();
    }
}
