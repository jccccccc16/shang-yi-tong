package com.cjc.syt.cmn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/3/27
 * Time: 9:54
 * To change this template use File | Settings | File Templates.
 **/
@SpringBootApplication
@ComponentScan(value = "com.cjc")
@MapperScan(value = "com.cjc.syt.cmn.mapper")
public class ServiceCmnApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCmnApplication.class,args);
    }
}
