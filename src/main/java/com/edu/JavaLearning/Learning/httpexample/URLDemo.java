package com.edu.JavaLearning.Learning.httpexample;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Tangzhihao
 * @date 2018/4/12
 */

public class URLDemo {

    public static void main(String[] args) throws IOException {
        String adress = "http://127.0.0.1:8848/myspringboot/httpPost.do?username=tang&password=123";

        URL url = new URL(adress);
        URLConnection connection = url.openConnection();
        InputStream in = connection.getInputStream();
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(isr);

        StringBuffer str = new StringBuffer();
        String s;
        while ((s = br.readLine())!=null){
            str.append(s);
        }
        System.out.println("url Data"+str);
    }
}
