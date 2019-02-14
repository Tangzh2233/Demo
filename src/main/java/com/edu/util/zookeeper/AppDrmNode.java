package com.edu.util.zookeeper;

import java.util.Objects;

/**
 * @Author: tangzh
 * @Date: 2019/1/21$ 6:59 PM$
 **/
public class AppDrmNode {
    private Object object;
    private String paramName;
    private String value;
    private String className;

    public AppDrmNode(Object object, String paramName, String value) {
        this.object = object;
        this.paramName = paramName;
        this.value = value;
        this.className = object.getClass().getSimpleName();
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Object getObject() {
        return object;
    }

    public String getParamName() {
        return paramName;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppDrmNode)) return false;
        AppDrmNode that = (AppDrmNode) o;
        return Objects.equals(getObject(), that.getObject()) &&
                Objects.equals(getParamName(), that.getParamName()) &&
                Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getObject(), getParamName(), getValue());
    }

    @Override
    public String toString() {
        return "AppDrmNode{" +
                "object=" + object +
                ", paramName='" + paramName + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
