package com.edu.service.quartz.job.Thread;

import com.edu.dao.domain.Alert;
import com.edu.dao.domain.CategoryDto;
import com.edu.dao.domain.Project;
import com.edu.dao.mapper.cat.AlertMapper;
import com.edu.dao.mapper.cat.ProjectMapper;
import com.edu.service.quartz.http.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Tangzhihao
 * @date 2018/3/22
 */

public class ReqAlarmThread implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(ReqAlarmThread.class);
    private final static String emailUrl = "http://127.0.0.1:8080/alarmcore/SendMsg/sendMail";
    private final static String weixinUrl = "http://127.0.0.1:8080/alarmcore/SendMsg/sendWeixin";
    private final static String smsUrl = "http://127.0.0.1:8080/alarmcore/SendMsg/sendSms";
    private String domain;
    private String strDate;
    private String channel;
    private AlertMapper alertMapper;
    private ProjectMapper projectMapper;

    public ReqAlarmThread(String domain,String strDate,String channel,AlertMapper alertMapper,ProjectMapper projectMapper){
        this.domain = domain;
        this.strDate = strDate;
        this.channel = channel;
        this.alertMapper = alertMapper;
        this.projectMapper = projectMapper;
    }

    @Override
    public void run() {
        logger.info("进入请求alarmcore线程");
        Map<String,String> params = new HashMap();
        Map<String,Integer> dtos = new HashMap();
        StringBuffer contents = new StringBuffer();
        String url = "";
        String toAddr = "";
        String sendType = "";
        //初始化dtos
        dtos.put("Transaction",0);
        dtos.put("Event",0);
        dtos.put("Business",0);


        //获取本日domain项目的报警记录
        List<Alert> alerts = alertMapper.selectAlertByDomainAndDate(domain, strDate);
        //获取不同category的告警总次数
        List<CategoryDto> categoryDtos = alertMapper.getCategory(domain, strDate);
        for (CategoryDto dto:categoryDtos){
            dtos.put(dto.getCategory(),dto.getCount());
        }
        
        Map metricsDetail = buildMetricsDetail(alerts);
        String content = buildContentDetail(contents, dtos,metricsDetail);
        logger.info("content: "+content);

        List<Project> project = projectMapper.selectEmailByDomain(domain);
        //移除无告警邮箱的project
        Iterator<Project> iterator = project.iterator();
        while (iterator.hasNext()){
            Project next = iterator.next();
            if(next.getEmail()==null||"".equals(next.getEmail())){
                iterator.remove();
            }
        }
        if(project==null){
            return;
        }
        if("Email".equals(channel)){
            toAddr = project.get(0).getEmail();
            url = emailUrl;
            sendType = "MAIL";
        }
        logger.info(" 发送人: "+toAddr);
        /*if("WeiXin".equals(channel)){
            toAddr = project.get(0).getOwner();
            url = weixinUrl;
            sendType = "WECHAT";
        }
        if("SMS".equals(channel)){
            toAddr = project.get(0).getPhone();
            url = smsUrl;
            sendType = "SMS";
        }*/

        params.put("appCode","cat");
        params.put("alarmType","1");
        params.put("alarmDesc","监控日报: "+strDate);
        params.put("alarmMessage",content);
        params.put("toAddr",toAddr);
        params.put("sendType",sendType);
        logger.info("http请求参数: " +params + url);
        String response = HttpClientUtil.doPost(url, params);
        logger.info("http响应参数:  "+response);
    }

    /**
     * @author: Tangzhihao
     * @date: 2018/4/11 14:30
     * @description:拼接告警详情
     */
    private String buildContentDetail(StringBuffer contents,Map map,Map<String,Map<String,Integer>> metrics){
        String tSize = String.valueOf(metrics.get("Transaction").size()+1);
        String eSize = String.valueOf(metrics.get("Event").size()+1);
        String bSize = String.valueOf(metrics.get("Business").size()+1);

        contents.append("你好!").append("<br />");
        contents.append(strDate + "  Cat监控日报信息如下：").append("<br />");
        contents.append("监控日报: ").append("<br /");
        //邮件追加堆栈信息
        contents.append("详情:").append("<br />");

        contents.append("<table border='1px' width='400px' cellpadding='2' cellspacing='0'>");
        contents.append("<tr><th>报警类型</th><th>报警总次数</th><th>报警指标</th><th>报警次数</th></tr>");

        //transaction行填充开始
        contents.append("<tr><td align= 'center' rowspan= '").append(tSize).append("'>Transaction</td><td align= 'center' rowspan= '").append(tSize).append("'>").append(map.get("Transaction")).append("</td></tr>");
        //transaction的Target-Time设置
        Map<String, Integer> transaction = metrics.get("Transaction");
        Iterator<Map.Entry<String, Integer>> Titerator = transaction.entrySet().iterator();
        while (Titerator.hasNext()){
            Map.Entry<String, Integer> next = Titerator.next();
            contents.append("<tr><td>").append(next.getKey()).append("</td>").append("<td>").append(next.getValue()).append("</td>").append("</tr>");
        }

        //Event行填充开始
        contents.append("<tr><td align= 'center' rowspan= '").append(eSize).append("'>Event</td><td align= 'center' rowspan= '").append(eSize).append("'>").append(map.get("Event")).append("</td></tr>");
        //Event的Target-Time设置
        Map<String, Integer> event = metrics.get("Event");
        Iterator<Map.Entry<String, Integer>> Eiterator = event.entrySet().iterator();
        while (Eiterator.hasNext()){
            Map.Entry<String, Integer> next = Eiterator.next();
            contents.append("<tr><td>").append(next.getKey()).append("</td>").append("<td>").append(next.getValue()).append("</td>").append("</tr>");
        }

        //Metric行填充开始
        contents.append("<tr><td align= 'center' rowspan= '").append(bSize).append("'>Metric</td><td align= 'center' rowspan= '").append(bSize).append("'>").append(map.get("Business")).append("</td></tr>");
        //Metric的Target-Time设置
        Map<String, Integer> metric = metrics.get("Business");
        Iterator<Map.Entry<String, Integer>> Miterator = metric.entrySet().iterator();
        while (Miterator.hasNext()){
            Map.Entry<String, Integer> next = Miterator.next();
            contents.append("<tr><td>").append(next.getKey()).append("</td>").append("<td>").append(next.getValue()).append("</td>").append("</tr>");
        }

        contents.append("</table>");
        return contents.toString();
    }

    /**
     * @author: Tangzhihao
     * @date: 2018/4/11 14:31
     * @description:将告警指标详情保存在Map中
     */
    private Map buildMetricsDetail(List<Alert> alerts){
        Map<String,Integer> Tetrics = new HashMap();
        Map<String,Integer> Eetrics = new HashMap();
        Map<String,Integer> Metrics = new HashMap();
        HashMap<String,Map<String,Integer>> metricMap = new HashMap();
        for (Alert alert:alerts) {
            if("Transaction".equals(alert.getCategory())){
                String t = alert.getMetric();
                if(Tetrics.containsKey(t)){
                    int i = Tetrics.get(t) + 1;
                    Tetrics.put(alert.getMetric(),i);
                }else{
                    Tetrics.put(t,1);
                }
            }
            if("Event".equals(alert.getCategory())){
                String e = alert.getMetric();
                if(Eetrics.containsKey(e)){
                    int i = Eetrics.get(e) + 1;
                    Eetrics.put(alert.getMetric(),i);
                }else{
                    Eetrics.put(e,1);
                }
            }
            if("Business".equals(alert.getCategory())){
                String b = alert.getMetric();
                if(Metrics.containsKey(b)){
                    int i = Metrics.get(b) + 1;
                    Metrics.put(alert.getMetric(),i);
                }else{
                    Metrics.put(b,1);
                }
            }
        }
        metricMap.put("Transaction",Tetrics);
        metricMap.put("Event",Eetrics);
        metricMap.put("Business",Metrics);
        return metricMap;
    }
}
