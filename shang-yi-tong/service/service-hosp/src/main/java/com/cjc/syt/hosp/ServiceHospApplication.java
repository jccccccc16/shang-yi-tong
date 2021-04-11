package com.cjc.syt.hosp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/3/22
 * Time: 20:14
 * To change this template use File | Settings | File Templates.
 **/
@SpringBootApplication
@ComponentScan(value = "com.cjc")
@MapperScan("com.cjc.syt.hosp.mapper")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.cjc")
public class ServiceHospApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceHospApplication.class,args);
    }
}
