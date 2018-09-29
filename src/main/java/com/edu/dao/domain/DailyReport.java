package com.edu.dao.domain;


import java.util.Map;

/**
 * @author Tangzhihao
 * @date 2018/4/19
 */

public class DailyReport {
    private String name;
    private Map<String,Integer> map;
    private Map<String,Map<String,Integer>> metricMap;

    public Map<String, Map<String, Integer>> getMetricMap() {
        return metricMap;
    }

    public void setMetricMap(Map<String, Map<String, Integer>> metricMap) {
        this.metricMap = metricMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Integer> getMap() {
        return map;
    }

    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }
}
