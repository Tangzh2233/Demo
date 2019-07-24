package com.edu.JavaLearning.freemarker;

import com.edu.dao.domain.DailyReport;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tangzhihao
 * @date 2018/4/19
 */

public class MyFreemarker{

    private static Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);

    public static void main(String[] args) {
        Map<String,Object> datas = new HashMap<>();
        List<String> namelist = new ArrayList<String>(){
            {
                add("tang");add("liu");add("yu");
            }
        };
        Map<String,String> map = new HashMap(){
            {
                put("tang","zhihao");
                put("yu","jiachun");
                put("liu","yuanju");
            }
        };
        datas.put("name","tang");
        datas.put("year","23");
        datas.put("class","一班");
        datas.put("domain","baidu");
        datas.put("namelist",namelist);
        DailyReport report = new DailyReport();
        report.setName("HAHAHAH");
        report.setMap(new HashMap<String, Integer>(){{put("Event",10);}});
        report.setMetricMap(new HashMap<String, Map<String, Integer>>(){
            {
                put("A",new HashMap<String, Integer>(){
                    {
                        put("tang",10);
                    }
                });
            }
        });
        datas.put("report",report);
        datas.put("map",map);
        String content = templateUtil( "/freemarker", datas);
        System.out.println(content);
    }

    public static String templateUtil(String tempPath,Map map){
        //设置加载器和模板路径
        configuration.setClassForTemplateLoading(MyFreemarker.class,tempPath);
        //新建模板输出流
        StringWriter sw = new StringWriter();
        try {
            //获取模板
            Template template = configuration.getTemplate("dailyReport.ftl");
            //模板替换，并加载到输出流中
            template.process(map,sw);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sw.toString();
    }
}
