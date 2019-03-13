package com.edu.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Author: tangzh
 * @Date: 2019/3/1$ 10:18 AM$
 **/
public class ZipFileUtil {

    /**
      * @description: Files->Zip
    **/
    public static void doFilesToZipAction(List<File> srcFiles, File zipFile) {
        // 判断压缩后的文件存在不，不存在则创建
        if (!zipFile.exists()) {
            try {
                zipFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 创建 FileOutputStream 对象
        FileOutputStream fileOutputStream = null;
        // 创建 ZipOutputStream
        ZipOutputStream zipOutputStream = null;
        // 创建 FileInputStream 对象
        FileInputStream fileInputStream = null;
        BufferedInputStream bis;
        try {
            // 实例化 FileOutputStream 对象
            fileOutputStream = new FileOutputStream(zipFile);
            // 实例化 ZipOutputStream 对象
            zipOutputStream = new ZipOutputStream(fileOutputStream);
            // 创建 ZipEntry 对象
            ZipEntry zipEntry = null;
            // 遍历源文件数组
            for (File file:srcFiles) {
                // 将源文件数组中的当前文件读入 FileInputStream 流中
                fileInputStream = new FileInputStream(file);
                bis = new BufferedInputStream(fileInputStream);
                // 实例化 ZipEntry 对象，源文件数组中的当前文件
                zipEntry = new ZipEntry(file.getName());
                zipOutputStream.putNextEntry(zipEntry);
                // 该变量记录每次真正读的字节个数
                int len;
                // 定义每次读取的字节数组
                byte[] buffer = new byte[1024];
                while ((len = bis.read(buffer)) > 0) {
                    zipOutputStream.write(buffer, 0, len);
                }
            }
            zipOutputStream.closeEntry();
            zipOutputStream.close();
            fileInputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static File init() {
        String a = "/users/tangzh/Downloads/照片/";
        List<String> urls = new ArrayList(){
            {
                add(a+"1.jpg");add(a+"2.jpg");add(a+"3.jpg");add(a+"4.jpg");add(a+"5.jpg");
                add(a+"6.jpg");add(a+"7.jpg");
            }
        };
//==============================================
//        List<File> srcFiles = new ArrayList<>();
//        for (String url:urls){
//            srcFiles.add(new File(url));
//        }
//        List<File> zipFiles = new ArrayList<>();
//        for (int i=0;i<2;i++){
//            File file = new File("/data/cache"+i+".zip");
//            zipFiles(srcFiles,file);
//            zipFiles.add(file);
//        }
//        File lastFile = new File("/data/cache.zip");
//        zipFiles(zipFiles,lastFile);
//        ======================================
        List<byte[]> bytes = getFileBytes(urls);
        List<File> lastZipFile = new ArrayList<>();
        for(int i=0;i<10;i++){
            File zipFile = new File("/data/merchant/cache/cache80001000001"+i+".zip");
            doByteToZipAction(bytes,zipFile);
            lastZipFile.add(zipFile);
        }
        File zip = new File("/data/merchant/cache.zip");
        doFilesToZipAction(lastZipFile,zip);
        return zip;
    }

    /**
      * @description: List<byte[]> -> Zip
    **/
    public static void doByteToZipAction(List<byte[]> srcFilesData,File zipFile) {
        if(!zipFile.exists()){
            try {
                zipFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
            int i = 0;
            for (byte[] bytes:srcFilesData){
                i = i+1;
                ZipEntry entry = new ZipEntry(""+i+".jpg");
                zos.putNextEntry(entry);
                zos.write(bytes);
            }
            zos.closeEntry();
            zos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
      * @description: FilePath -> List<byte[]>
    **/
    public static List<byte[]> getFileBytes(List<String> filePath){
        List<byte[]> rspData = new ArrayList<>();
        for (String path:filePath){
            try {
                File file = new File(path);
                FileInputStream fis = new FileInputStream(file);
                byte[] bytes = new byte[(int)file.length()];
                fis.read(bytes);
                fis.close();
                rspData.add(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return rspData;
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }else{
            // 目录此时为空，可以删除
            return dir.delete();
        }
        return true;
    }

}
