package com.edu.dao.domain;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Tangzh
 * @version 1.0.0
 * @date 2020/6/24 09:39
 * @description
 **/
@Data
@Builder
@Table(name = "tk")
public class TK {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private Long id;

    @Column(name = "user_no")
    private String userNo;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "ip_address")
    private String ipAddress;

    private String phone;

    //⚠️tk框架不支持LocalDateTime格式
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}
