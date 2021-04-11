package com.cjc.repositry;

import com.cjc.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/3/30
 * Time: 16:49
 * To change this template use File | Settings | File Templates.
 **/
public interface UserRepository extends MongoRepository<User,String> {
}
