package com.edu.JavaLearning.io.io;

import java.io.*;
import java.util.zip.*;

/**
 * @author Tangzhihao
 * @date 2018/3/5
 */

public class GZipAndZip {

    static String txt = "D:\\GZip.txt";
    static String gzip = "D:\\GZip.gzip";
    static String zip = "D:\\Zip.zip";

    public static void main(String[] args) throws IOException {
    //    DataGZipOut(txt,gzip);
    //    DataGZipIn(gzip,txt);
    //    DataZipOut(txt,zip);
        DataZipIn(txt,zip);
    }

    //GZip压缩文件
    public static void DataGZipOut(String in,String out) throws IOException {
        //获取目标文件的字符流
        BufferedReader bufferedReader = new BufferedReader(new FileReader(in));
        //
        BufferedOutputStream outputStream = new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(out)));
        int Iout;
        while ((Iout=bufferedReader.read())!=-1){
            outputStream.write(Iout);
        }
        bufferedReader.close();
        outputStream.close();
        System.out.println("压缩完成");
    }

    //GZip解压文件
    public static void DataGZipIn(String in,String out) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(out))));
        String str;
        while ((str = reader.readLine())!=null){
            System.out.println(str);
        }
        reader.close();
    }

    //Zip压缩
    public static void DataZipOut(String in,String out) throws IOException {
        //获取目标文件字符流
        BufferedReader brIn = new BufferedReader(new FileReader(in));

        FileOutputStream fos = new FileOutputStream(out);
        //生成了校检码，其中有两种方式：Adler32(快一些)和CRC32(慢一些，但更安全)。
        CheckedOutputStream cos = new CheckedOutputStream(fos, new Adler32());
        ZipOutputStream zos = new ZipOutputStream(cos);
        BufferedOutputStream bfOut = new BufferedOutputStream(zos);

        //BufferedOutputStream bfOut = new BufferedOutputStream(new ZipOutputStream(new CheckedOutputStream(new FileOutputStream(out), new Adler32())));

        zos.putNextEntry(new ZipEntry("GZip.txt"));
        int strOut;
        while ((strOut=brIn.read())!=-1){
           // zos.write(strOut);
            bfOut.write(strOut);
        }
        brIn.close();
        bfOut.flush();
        bfOut.close();
        System.out.println("压缩完成");
    }

    //Zip解压
    public static void DataZipIn(String in,String Out) throws IOException {
        FileInputStream fis = new FileInputStream(Out);
        CheckedInputStream cis = new CheckedInputStream(fis, new Adler32());
        ZipInputStream zis = new ZipInputStream(cis);
        InputStreamReader isr = new InputStreamReader(zis);
        BufferedReader br = new BufferedReader(isr);
        //    BufferedReader br = new BufferedReader(new InputStreamReader(new ZipInputStream(new CheckedInputStream(new FileInputStream(Out), new Adler32()))));
        ZipEntry ze;
        while ((ze=zis.getNextEntry())!=null){
            String StrOut;
            while ((StrOut=br.readLine())!=null){
                System.out.println(StrOut);
            }
        }
        br.close();
    }
}
