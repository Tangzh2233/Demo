package com.edu.JavaLearning.io;

import sun.misc.Cleaner;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/12 2:40 PM
 * 文件切分。堆内对外内存速度对比
 * https://www.cnblogs.com/lyftest/p/6564547.html
 *
 * 原来内存映射文件的效率比标准IO高的重要原因:
 * 就是因为少了把数据拷贝到OS内核缓冲区这一步（可能还少了native堆中转这一步）。
 **/
public class SplitFile {

    private static int bufferSize = 1024;

    public static void main(String[] args) throws IOException {
        SplitFile splitFile = new SplitFile();
        readFile("/data/logs/tbmmgr/wx.dmg");
        readFile2("/data/logs/tbmmgr/wx.dmg");
        readFile3("/data/logs/tbmmgr/wx.dmg");
        splitFile.doSplitFile("/data/logs/tbmmgr/root.log", 4);
//        String[] files = new String[]{"/data/logs/tbmmgr/root0.log", "/data/logs/tbmmgr/root1.log", "/data/logs/tbmmgr/root2.log", "/data/logs/tbmmgr/root3.log"};
//        splitFile.mergeFile(files, "/data/logs/tbmmgr/root-merge.log");
    }

    /**
     * 文件拆分
     *
     * @param filePath
     * @param splitCount
     * @throws IOException
     */
    public void doSplitFile(String filePath, int splitCount) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        FileChannel channel = fileInputStream.getChannel();
        long fileSize = channel.size();
        long average = fileSize / splitCount;
        //此处需要在堆上分配内存,即文件数据拷贝到堆上再操作
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
//        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        //创建直接内存和文件数据映射
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        map.get(new byte[1024]);//直接读入到byte[]数组中
        long startPosition = 0;
        for (int i = 0; i < splitCount; i++) {
            channel.read(byteBuffer, startPosition);
            FileOutputStream outputStream = new FileOutputStream(filePath.substring(0, filePath.indexOf(".")) + i + ".log");
            FileChannel outChannel = outputStream.getChannel();
            channel.transferTo(startPosition, average, outChannel);
            outChannel.close();
            outputStream.close();
            startPosition += average;
        }
        channel.close();
        fileInputStream.close();
    }

    /**
     * 文件合并
     *
     * @param mergeFileNames
     * @param fileName
     */
    public void mergeFile(String[] mergeFileNames, String fileName) {
        try {
            RandomAccessFile accessFile = new RandomAccessFile(fileName, "rw");
            //每次读1024字节
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int startIndex = 0;
            for (int i = 0; i < mergeFileNames.length; i++) {
                accessFile.seek(startIndex);
                FileInputStream fs = new FileInputStream(mergeFileNames[i]);
                FileChannel channel = fs.getChannel();
                startIndex += channel.size();
                while (true) {
                    int read = channel.read(byteBuffer);
                    if (read == -1) {
                        break;
                    }
                    accessFile.write(byteBuffer.array(), 0, read);
                    byteBuffer.clear();
                }
                channel.close();
                fs.close();
            }
            accessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 普通NIO方式读取文件
     * buffer分配在堆上
     * @see SplitFile#bufferSize 效率受bufferSize的影响很大
     * 数据流动方向
     * 硬盘 -> OS内核缓冲区 -> native堆 -> jvm堆 -> 私有进程引用
     * @param filePath
     * @throws IOException
     */
    public static void readFile(String filePath) throws IOException {
        FileInputStream inputStream = new FileInputStream(filePath);
        FileChannel channel = inputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        long begin = System.currentTimeMillis();
        while (true) {
            int read = channel.read(buffer);
            if (read == -1) {
                break;
            }
            buffer.flip();
            buffer.clear();
        }
        System.out.println("堆内buffer耗时:" + (System.currentTimeMillis() - begin) + "ms");
        channel.close();
        inputStream.close();
    }

    /**
     * NIO读取操作
     * buffer分配在直接内存的
     * @see SplitFile#bufferSize 效率受bufferSize的影响很大
     * 和上边的区别就是堆内和对外,yongGC不用回收这一块,fullGC时回收
     * 数据流动方向
     * 硬盘 -> OS内核缓冲区 -> native堆 -> jvm堆外直接内存 -> 私有进程引用
     * @param filePath
     * @throws IOException
     */
    public static void readFile2(String filePath) throws IOException {
        FileInputStream inputStream = new FileInputStream(filePath);
        FileChannel channel = inputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocateDirect(bufferSize);
        long begin = System.currentTimeMillis();
        while (true) {
            int read = channel.read(buffer);
            if (read == -1) {
                break;
            }
            buffer.flip();
            buffer.clear();
        }
        System.out.println("堆外buffer耗时:" + (System.currentTimeMillis() - begin) + "ms");
        channel.close();
        inputStream.close();
    }

    /**
     * NIO读取操作
     * 内存映射的方式
     * @see SplitFile#bufferSize 基本不受bufferSize的影响
     * map在直接内存,做堆和硬件文件的映射
     * 硬盘 ->mmap -> jvm堆外直接内存
     * @param filePath
     * @throws IOException
     */
    public static void readFile3(String filePath) throws IOException {
        FileInputStream inputStream = new FileInputStream(filePath);
        FileChannel channel = inputStream.getChannel();
        long length = channel.size();
        int size = bufferSize;
        byte[] bytes = new byte[size];
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_ONLY, 0, length);
        long begin = System.currentTimeMillis();
        //for循环中的length一定不要再使用channel.size()的方式获取,否则耗费时间让你怀疑人生
        for (int i = 0; i < length; i += size) {
            if (length - i > size) {
                map.get(bytes);
            } else {
                map.get(new byte[(int) length - i]);
            }
        }
        System.out.println("内存映射:" + (System.currentTimeMillis() - begin) + "ms");
        clean(map);
        channel.close();
        inputStream.close();
    }

    /**
     * mmp 清理
     * @param directBuffer
     */
    private static void clean(MappedByteBuffer directBuffer) {
        AccessController.doPrivileged((PrivilegedAction<Object>) () ->
        {
            try {
                Method cleaner = directBuffer.getClass().getMethod("cleaner");
                cleaner.setAccessible(true);
                Cleaner invoke = (Cleaner) cleaner.invoke(directBuffer, new Object[0]);
                invoke.clean();
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        });
    }
}
