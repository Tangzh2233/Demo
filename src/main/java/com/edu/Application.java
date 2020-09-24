package com.edu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Created by Administrator on 2017/8/4.
 */
@EnableAsync
@ImportResource("classpath*:/spring/*.xml")
@MapperScan("com.edu.dao.mapper.**.*")
@SpringBootApplication(scanBasePackages = {"com.edu"},exclude = {HibernateJpaAutoConfiguration.class})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }

}
