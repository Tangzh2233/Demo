package com.edu.JavaLearning.算法;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/12 2:40 PM
 * 文件切分
 **/
public class SplitFile {


    public static void main(String[] args) throws IOException {
        SplitFile splitFile = new SplitFile();
        splitFile.doSplitFile("/data/logs/tbmmgr/root.log", 4);

        String[] files = new String[]{"/data/logs/tbmmgr/root0.log", "/data/logs/tbmmgr/root1.log", "/data/logs/tbmmgr/root2.log", "/data/logs/tbmmgr/root3.log"};
        splitFile.mergeFile(files, "/data/logs/tbmmgr/root-merge.log");
    }

    /**
     * 文件拆分
     * @param filePath
     * @param splitCount
     * @throws IOException
     */
    public void doSplitFile(String filePath, int splitCount) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        FileChannel channel = fileInputStream.getChannel();
        long fileSize = channel.size();
        long average = fileSize / splitCount;
//        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        long startPosition = 0;
        for (int i = 0; i < splitCount; i++) {
//            channel.read(byteBuffer, startPosition);
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
     * @param mergeFileNames
     * @param fileName
     */
    public void mergeFile(String[] mergeFileNames, String fileName) {
        try {
            RandomAccessFile accessFile = new RandomAccessFile(fileName, "rw");
            //每次读20字节
            ByteBuffer byteBuffer = ByteBuffer.allocate(20);
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
}
