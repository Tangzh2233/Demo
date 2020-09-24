package com.edu.dao.domain;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.text.Collator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2017/8/4.
 * 注解的意思:Null 不进行json序列化。代表序列化规则
 */
@Data
public class User implements Serializable,Comparable<User>{

    private static final long serialVersionUID = -3677823825852900686L;

    public User() {
    }

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String userNo) {
        this.username = username;
        this.password = password;
        this.userNo = userNo;
    }

    public User(Integer id, String username, String password, LocalDateTime date, String userNo) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.date = date;
        this.userNo = userNo;
    }

    private Integer id;
    @NotBlank(message = "用户名不可为空")
    private String username;
    @NotBlank(message = "password不可为空")
    private String password;
    @Min(1)
    @Max(100)
    private Long num;
    @NotNull
    private LocalDateTime date;
    private String userNo;
    @AssertTrue
    private Boolean is_delete;
    private List<String> idList;

    @Email
    private String email;
    @javax.validation.constraints.Pattern(regexp = "^1(3|4|5|7|8)\\d{9}$")
    private String phoneNum;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    /**
     * 依据姓名拼音排序
     * @param user
     * @return
     */
    @Override
    public int compareTo(@NotNull User user) {
        return Collator.getInstance(Locale.CHINESE).compare(this.getUsername(),user.getUsername());
    }

    static class Node {
        private String head;
        private String tail;

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getTail() {
            return tail;
        }

        public void setTail(String tail) {
            this.tail = tail;
        }
    }

    public static void main(String[] args) {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setId(i);
            user.setUsername("tang");
            user.setPassword("123");
            userList.add(user);
        }
        for (int i = 5; i < 10; i++) {
            User user = new User();
            user.setId(i);
            user.setUsername("zhang");
            user.setPassword("qwe");
            userList.add(user);
        }
        List<Integer> collect = userList.stream().filter(user -> "zhang".equals(user.getUsername()))
                .map(User::getId).collect(Collectors.toList());
        for(Integer item : collect){
            System.out.println(item);
        }

        Matcher matcher = Pattern.compile("(\\d{3})\\d{4}(\\d{4})").matcher("17010206231");
        while (matcher.find()){
            System.out.println(matcher.group(0));
            System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));
        }
        System.out.println("userId".replaceAll("([A-Z]+)","_$1").toLowerCase());
        System.out.println("17010206231".replaceAll("(\\d{3})\\d{4}(\\d{4})","$2****$1"));
    }

}
