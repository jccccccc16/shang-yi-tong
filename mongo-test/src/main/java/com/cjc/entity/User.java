package com.cjc.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/3/30
 * Time: 12:39
 * To change this template use File | Settings | File Templates.
 **/
@Data
@Document("User") // 绑定mongodb中的一个名为user的集合
public class User {

    @Id // 指定该id为mongo自增的_id
    private String id;

    private String name;

    private Integer age;

    private String email;

    private String createDate;

}
