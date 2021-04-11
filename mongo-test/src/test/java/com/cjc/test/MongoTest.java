package com.cjc.test;

import com.cjc.entity.User;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/3/30
 * Time: 12:41
 * To change this template use File | Settings | File Templates.
 **/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void save(){

        User user = new User();
        user.setAge(20);
        user.setName("cjc");
        user.setEmail("123@123.com");
        // 带id的user，插入到数据库
        User insertUser = mongoTemplate.insert(user);

        // 插入到集合
//        mongoTemplate.insert(user,"Student");

        log.info(insertUser+"");
    }

    /**
     * 查询
     */
    @Test
    public void findAll(){
        List<User> students = mongoTemplate.findAll(User.class, "student");
        //mongoTemplate.findAll(User.class);
    }

    @Test
    public void findById(){
        User user = mongoTemplate.findById("id", User.class);

    }

    /**
     * 根据条件查询
     */
    @Test
    public void findList(){
        List<User> users = mongoTemplate.find(new Query(Criteria.where("key").is("value").and("age")), User.class);
    }

    /**
     * 模糊查询
     */
    @Test
    public void findLike(){
        // 匹配规则
        String name = "cjc";
        String regex = String.format("%s%s%s","^.*",name,".*$");
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

        Query query = new Query(Criteria.where("name").regex(pattern));
        mongoTemplate.find(query,User.class);
    }

    /**
     * 分页查询
     */
    @Test
    public void findPage(){
        Integer pageNo = 10;
        Integer pageSize = 5;
        Query query = new Query();
        query.skip((pageNo - 1)*pageSize).limit(pageSize);
        mongoTemplate.find(query,User.class);
    }

    @Test
    public void update(){
        Query query = new Query(Criteria.where("id").is("111"));
        Update update = new Update();
        update.set("name","cjc");
        UpdateResult result = mongoTemplate.upsert(query, update, User.class);
        // 影响的记录数
        long modifiedCount = result.getModifiedCount();
    }

    @Test
    public void deleteUser(){
        Query query = new Query(Criteria.where("id").is("111"));
        mongoTemplate.remove(query,User.class,"student");

    }






}
