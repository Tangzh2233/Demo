package com.edu.dao.domain;

import java.util.Date;

/**
 * @author Tangzhihao
 * @date 2018/3/19
 */

public class Alert {

    //主键
    private Long id;
    //项目名
    private String domain;
    //告警时间
    private Date alertTime;
    //告警分类
    private String category;
    //告警级别
    private String type;
    //告警内容
    private String content;
    //告警指标
    private String metric;
    //private Date creationDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String doMain) {
        this.domain = doMain;
    }

    public Date getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(Date alertTime) {
        this.alertTime = alertTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }
}
