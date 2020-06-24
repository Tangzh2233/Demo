package com.edu.JavaLearning.io;


import java.io.*;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 * 字符集+字符编码关系认知
 * https://blog.csdn.net/weixin_42167759/article/details/80421206
 * <p>
 * https://www.cnblogs.com/lyftest/p/6564547.html
 * Buffer类的作用。减少系统调用次数,即减少了用户态和内核态的切换
 * OS内核缓冲区的作用。减少磁盘IO操作
 *
 * @author Tangzhihao
 * @date 2018/4/27
 * 基础字节输入输出流
 * @see InputStream
 * @see InputStream#read(),InputStream#read(byte[]),InputStream#read(byte[], int, int)
 * //从输入流中读出一个字节 || 从输入流中读取一定数量的字节到byte中 || 从输入流中读取一定数量的字节到byte中
 * @see InputStream#mark(int),InputStream#reset(),InputStream#skip(long), //在此输入流中标记当前位置
 * mark记录 mark=pos,reset将pos->mark,skip n 跳过n个字节
 * @see OutputStream
 * @see ByteArrayInputStream,ByteArrayOutputStream
 * @see BufferedInputStream,BufferedOutputStream,StringReader
 * @see DataInputStream,DataOutputStream
 * @see FilterInputStream,FilterOutputStream
 * @see PushbackInputStream#read(byte[]),PushbackInputStream#unread(byte[])
 * //将byte[]数组读入到Pushback的内置byte[]中。取消读取的意思
 * @see FileInputStream,FileOutputStream
 * @see ObjectInputStream,ObjectOutputStream
 */

public class InOutPutStream {

    private static int size = 32;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        endeCode();
//        BaseInPutStreamOfByteArray();
//        dataInputStream();
//        readFileByInStream();
//        readFileByInStreamBuffer();
//        readFileByReaderBuffer();
        readFileByAccessFile();
//        readFileByNIO();
//        readFileByNIO2();
//        readFileByNIO3();
        objectInOutStream();
    }

    /**
     * 字符集: 定义了字符和二进制的对应关系 a -> 01100001; A -> 01000001
     * 编码: 本质上是字符到字节的转换过程
     * 解码: 字节到字符的转换过程
     */
    public static void endeCode() {
        String data = "蒙多,飘牙是个大傻子!";
        //获取utf8编码器
        Charset uft8 = Charset.forName("UTF-8");
        //获取gbk编码器
        Charset gbk = Charset.forName("GBK");

        //1.用utf8的方式将data编码为字节数组
        ByteBuffer byteBuffer = uft8.encode(data);
        //2.用gbk的方式将utf8编码的byte字节数组解码为char字符数组
        CharBuffer charBuffer = gbk.decode(byteBuffer);
        //此时输出,将得不到原数据,因为两者编码方式不兼容
        System.out.println("原数据:" + data);
        System.out.println("编码解码后数据:" + charBuffer);
    }


    public static void BaseInPutStreamOfByteArray() throws IOException {
        byte[] bytes = new byte[]{1, 2, 3, 4, 5, 6};
        byte[] context = new byte[bytes.length];
        //byte数组作为数据源,读入流中
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        int i = 0;
        while ((i = bais.read()) != -1) {
            System.out.println(i);
        }
        bais.close();
    }

    /**
     * @throws IOException
     * @see DataInputStream,DataOutputStream
     * 字节数组 <-> java基本类型的转换
     * 将字节数组按着给定类型读出或写入
     * 1.将[1,2,3,4,5,6]按照char类型读出,因为一个char占2个字节,所以这里循环3次.具体实现可具体查看
     * 2.将"飘牙是个大傻子"读入out中,再打印输出。相当于一个转换的作用
     */
    public static void dataInputStream() throws IOException {
        byte[] in = new byte[]{1, 2, 3, 4, 5, 6};
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(in));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(out);
        for (int i = 0; i < 3; i++) {
            System.out.println(dataInputStream.readChar());
        }
        dataOutputStream.writeChars("飘牙是个大傻子");
        System.out.println(out.toString());
        out.close();
        dataInputStream.close();
        dataOutputStream.close();
    }

    /**
     * 使用字节数组的方式读取文件,未使用buffer缓冲区
     *
     * @throws IOException
     */
    public static void readFileByInStream() throws IOException {
        File file = new File("/data/GZip.txt");
        FileInputStream in = new FileInputStream(file);
        FileOutputStream out = new FileOutputStream("/data/GZip-1.txt");
        byte[] bytes = new byte[size];
        long length = file.length();
        long begin = System.currentTimeMillis();
        for (int i = 0; i < length; i += size) {
            //数据读入
            if (in.read(bytes) == -1) {
                break;
            }
            //数据写出
            if (length - i >= size) {
                out.write(bytes);
            } else {
                out.write(bytes, 0, (int) length - i);
            }
        }
        System.out.println("InOutStream-File1 :" + (System.currentTimeMillis() - begin) + "ms");
        out.close();
        in.close();
    }

    /**
     * 使用字节数组的方式读取文件,使用buffer缓冲区
     *
     * @throws IOException
     */
    public static void readFileByInStreamBuffer() throws IOException {
        File file = new File("/data/GZip.txt");
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("/data/GZip-2.txt"));
        byte[] bytes = new byte[size];
        long length = file.length();
        long begin = System.currentTimeMillis();
        for (int i = 0; i < length; i += size) {
            if ((in.read(bytes) == -1)) {
                break;
            }
            if (length - i >= size) {
                out.write(bytes);
            } else {
                out.write(bytes, 0, (int) length - i);
            }
        }
        System.out.println("BufferInOutStream-File2: " + (System.currentTimeMillis() - begin) + "ms");
        out.close();
        in.close();
    }

    /**
     * 使用字符数组的方式读取文件.
     * 测试结果  字节数组 > 字符缓冲 > 字节缓冲
     *
     * @throws IOException
     * @see BufferedReader#readLine() 读取一行
     */
    public static void readFileByReaderBuffer() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("/data/GZip.txt"));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("/data/GZip-3.txt"));
        String line;
        long begin = System.currentTimeMillis();
        //按行读取
//        while ((line = bufferedReader.readLine()) != null) {
//            bufferedWriter.write(line);
//        }
        //按char数组读取
        char[] chars = new char[size];
        while ((bufferedReader.read(chars)) != -1) {
            bufferedWriter.write(chars);
        }
        System.out.println("BufferReader-File3: " + (System.currentTimeMillis() - begin) + "ms");
        bufferedWriter.close();
        bufferedReader.close();
    }

    /**
     * RandomAccessFile方式读取文件
     *
     * @throws IOException
     */
    public static void readFileByAccessFile() throws IOException {
        //设置连接文件以及对文件的读写权限
        RandomAccessFile in = new RandomAccessFile("/data/GZip.txt", "r");
        RandomAccessFile out = new RandomAccessFile("/data/GZip-4.txt", "rw");
        byte[] bytes = new byte[size];
        long length = in.length();
        long begin = System.currentTimeMillis();
        for (int i = 0; i < length; i += size) {
            if ((in.read(bytes) == -1)) {
                break;
            }
            if (length - i >= size) {
                out.write(bytes);
            } else {
                out.write(bytes, 0, (int) length - i);
            }
        }
        System.out.println("AccessFile-File4: " + (System.currentTimeMillis() - begin) + "ms");
        out.close();
        in.close();
    }

    /**
     * NIO方式读取文件
     * 文件数据流
     * 磁盘 -> 内核缓冲区 -> native堆 -> java堆(ByteBuffer)
     * 且因为ByteBuffer大小的限制需要频繁write
     *
     * @throws IOException
     */
    public static void readFileByNIO() throws IOException {
        FileInputStream in = new FileInputStream("/data/GZip.txt");
        FileOutputStream out = new FileOutputStream("/data/GZip-5.txt");
        FileChannel inChannel = in.getChannel();
        FileChannel outChannel = out.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(size);
        long length = inChannel.size();
        long begin = System.currentTimeMillis();
        for (int i = 0; i < length; i += size) {
            if (inChannel.read(buffer) == -1) {
                break;
            }
            buffer.flip();
            outChannel.write(buffer);
            buffer.clear();
        }
        System.out.println("NIO-File5: " + (System.currentTimeMillis() - begin) + "ms");
        outChannel.close();
        inChannel.close();
    }

    /**
     * NIO2方式读取文件
     * 磁盘 -> 内核缓冲区 -> native堆 -> java堆(ByteBuffer)
     * 直接inChannel -> outChannel 一次性write
     * 零拷贝
     *
     * @throws IOException
     */
    public static void readFileByNIO2() throws IOException {
        FileInputStream in = new FileInputStream("/data/GZip.txt");
        FileOutputStream out = new FileOutputStream("/data/GZip-6.txt");
        FileChannel inChannel = in.getChannel();
        FileChannel outChannel = out.getChannel();

        long length = inChannel.size();
        long begin = System.currentTimeMillis();
        //数据写入,直接从inChannel -> outChannel
        inChannel.transferTo(0, length, outChannel);
        System.out.println("NIO2-File6: " + (System.currentTimeMillis() - begin) + "ms");
        outChannel.close();
        inChannel.close();
        out.close();
        in.close();
    }

    /**
     * NIO3方式读取文件
     * 磁盘 -> 堆外[MappedByteBuffer]
     * 数组直接从堆外内存一次性写入文件
     *
     * @throws IOException
     */
    public static void readFileByNIO3() throws IOException {
        FileInputStream in = new FileInputStream("/data/GZip.txt");
        FileOutputStream out = new FileOutputStream("/data/GZip-7.txt");
        FileChannel inChannel = in.getChannel();
        long length = inChannel.size();

        MappedByteBuffer map = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, length);
        FileChannel outChannel = out.getChannel();
        long begin = System.currentTimeMillis();
        //数据写入
        outChannel.write(map);

        System.out.println("NIO3-File7: " + (System.currentTimeMillis() - begin) + "ms");
        outChannel.close();
        inChannel.close();
        out.close();
        in.close();
    }

    /**
     * 对象 -> 字节数组
     * 字节数组 -> 对象
     *
     * @see ObjectInputStream,ObjectOutputStream
     */
    public static void objectInOutStream() throws IOException, ClassNotFoundException {
        InOutObject in = new InOutObject();
        in.setName("糖糖");
        in.setTrue(true);
        in.setAmount(new BigDecimal("1300.01"));
        in.setHigh(22.22);
        in.setIdList(Arrays.asList("tangzh"));

        FileOutputStream out = new FileOutputStream("/data/InOutObject.class");
        ObjectOutputStream outStream = new ObjectOutputStream(out);
        outStream.writeObject(in);
        System.out.println("序列化完成");
        outStream.close();
        out.close();

        FileInputStream inputStream = new FileInputStream("/data/InOutObject.class");
        ObjectInputStream inStream = new ObjectInputStream(inputStream);
        InOutObject object = (InOutObject) inStream.readObject();
        System.out.println(object.toString());
        inStream.close();
        inputStream.close();
    }

    /**
     * 自定义序列化+反序列化
     *
     * @see InOutObject#readObject(ObjectInputStream)
     * @see InOutObject#writeObject(ObjectOutputStream)
     *
     * ⚠️属性序列化写入的顺序和反序列化读出的赋值需要一一对应
     */
    static class InOutObject implements Serializable {

        private static final long serialVersionUID = -8739291334534837669L;
        private transient int id;
        private String name;
        private BigDecimal amount;
        private boolean isTrue;
        private double high;
        private List<String> idList;

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            this.amount = (BigDecimal) in.readObject();
            this.name = in.readUTF();
            this.isTrue = in.readBoolean();
            this.high = in.readDouble();
            this.idList = (List<String>) in.readObject();
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.writeObject(this.amount);
            out.writeUTF(this.name);
            out.writeBoolean(this.isTrue);
            out.writeDouble(this.high);
            out.writeObject(this.idList);
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public boolean isTrue() {
            return isTrue;
        }

        public void setTrue(boolean aTrue) {
            isTrue = aTrue;
        }

        public double getHigh() {
            return high;
        }

        public void setHigh(double high) {
            this.high = high;
        }

        public List<String> getIdList() {
            return idList;
        }

        public void setIdList(List<String> idList) {
            this.idList = idList;
        }

        @Override
        public String toString() {
            return "InOutObject{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", amount=" + amount +
                    ", isTrue=" + isTrue +
                    ", high=" + high +
                    ", idList=" + idList +
                    '}';
        }
    }

}
