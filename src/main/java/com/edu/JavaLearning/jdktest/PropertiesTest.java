package com.edu.JavaLearning.jdktest;

import java.io.*;
import java.util.Properties;

/**
 * @author Tangzhihao
 * @date 2017/12/15
 */
public class PropertiesTest {

    private static Properties pop;

    /*public void init(){
        pop = new Properties();
        InputStream in = Object.class.getResourceAsStream("/feemapper.properties");

        try {
            pop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/

    static {
        pop = new Properties();
       // InputStream in = Object.class.getResourceAsStream("/feemapper.properties");
        InputStream inputStream = PropertiesTest.class.getClassLoader().getResourceAsStream("feemapper.properties");
        try {
            pop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {

        System.out.println(pop.get("JBS_0801_GRJY"));
        String s = pop.getProperty("JBS_0801_GRJY");
        String s1 = pop.getProperty("JBS_0802_PT");
        System.out.println(s);
        System.out.println(s1);

        //读数据，写数据
    //    readProperties("feemapper.properties");
    //    writeProperties("D:\\springboot\\src\\main\\resources\\dev-config.properties");

    }

    public static void readProperties(String fileName) throws IOException {
        pop = new Properties();
        InputStream in = PropertiesTest.class.getClassLoader().getResourceAsStream(fileName);
        pop.load(in);
    }

    public static void writeProperties(String fileName) throws IOException {
        FileOutputStream out = new FileOutputStream(fileName,true);
        pop.store(out,"Detail");
    }
}
