package com.cjc.yygh.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/4/17
 * Time: 16:45
 * To change this template use File | Settings | File Templates.
 **/
@SpringBootApplication
@MapperScan("com.cjc.yygh.user.mapper")
@EnableDiscoveryClient
@ComponentScan("com.cjc")
public class UserInfoApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserInfoApplication.class,args);
    }
}
