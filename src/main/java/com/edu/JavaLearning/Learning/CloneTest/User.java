package com.edu.JavaLearning.Learning.CloneTest;

/**
 * Created by Administrator on 2017/8/15.
 */
public class User implements Cloneable{
    private int id;
    private StringBuffer name;
    private int[] password;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        User user;
        user = (User) super.clone();
        user.name = new StringBuffer(name);
        user.password = password.clone();
        return user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StringBuffer getName() {
        return name;
    }

    public void setName(StringBuffer name) {
        this.name = name;
    }

    public int[] getPassword() {
        return password;
    }

    public void setPassword(int[] password) {
        this.password = password;
    }
}
