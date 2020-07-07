package com.edu.JavaLearning.io;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Tangzh
 * @version 1.0.0
 * @date 2020/6/30 16:01
 * @description 网络数据读取
 **/
public class NetInOutPutStream {

    private static int buffer_size = 512;

    public static void main(String[] args) throws Exception {
        String url = "https://static.91jkys.com/upload/202006/30/11f0f0641d544d149c0d5bc1043660e7.jpg";
        long start = System.currentTimeMillis();
        byte[] bytes = readNetInputStream(url);
        System.out.println( "耗时" + (System.currentTimeMillis() - start) + "ms");
    }


    /**
     * 获取网络数据
     * @param fileUrl
     * @return
     * @throws Exception
     */
    public static byte[] readNetInputStream(String fileUrl) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            HttpURLConnection connection = (HttpURLConnection)new URL(fileUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(1000);

            InputStream inputStream = connection.getInputStream();
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                byte[] buffer = new byte[buffer_size];
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                int len;
                while ( (len = bis.read(buffer)) != -1){
                    bos.write(buffer,0,len);
                }
                bis.close();
                buffer = null;
            }
            inputStream.close();
            return bos.toByteArray();
        } catch (IOException e) {
            throw e;
        } finally {
            bos.close();
        }
    }
}
