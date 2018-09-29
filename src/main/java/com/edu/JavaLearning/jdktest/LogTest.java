package com.edu.JavaLearning.jdktest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.edu.JavaLearning.jdktest.ENUM.EChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * @author Tangzhihao
 * @date 2018/1/4
 */

public class LogTest {
    private static  final Logger logger = LoggerFactory.getLogger(LogTest.class);
    public static void main(String[] args) {
        Resp resp = new Resp();
        Resp.Data data = new Resp().new Data();
        Resp.Error error = new Resp().new Error();
        data.setDcode("0");data.setDdesc("成功");
        error.setEcode("1");error.setEdesc("失败");
        resp.setData(data);
        resp.setError(error);
        String json = JSON.toJSONString(resp);
        System.out.println(json);
    //    String json = "{'id':'1','name':'tang'}";
        JSONObject object = JSON.parseObject(json);
        System.out.println(object.getJSONObject("data").get("dcode"));
    /*    logger.info("--------------info");
        logger.debug("-------------debug");
        logger.warn("-------------warn");
        logger.error("--------------error");
        for (int i=1;i<6;i++){
            EChannel eChannel = EChannel.EChannel_Map.get(i);
            System.out.println(eChannel.getDesc());
        }
        try {
            Class<?> aClass = Class.forName("com.edu.JavaLearning.jdktest.LogTest");
            Class<?>[] interfaces = aClass.getInterfaces();
            for (Class<?> a:interfaces) {
                System.out.println(a.getSimpleName()+"22");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/
    }
}

class Resp{
    private Error error;
    private Data data;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    class Error{
        private String Ecode;
        private String Edesc;

        public String getEcode() {
            return Ecode;
        }

        public void setEcode(String ecode) {
            Ecode = ecode;
        }

        public String getEdesc() {
            return Edesc;
        }

        public void setEdesc(String edesc) {
            Edesc = edesc;
        }
    }
    class Data{
        private String Dcode;
        private String Ddesc;

        public String getDcode() {
            return Dcode;
        }

        public void setDcode(String dcode) {
            Dcode = dcode;
        }

        public String getDdesc() {
            return Ddesc;
        }

        public void setDdesc(String ddesc) {
            Ddesc = ddesc;
        }
    }
}
