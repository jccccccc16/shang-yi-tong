package com.cjc.syt.hosp.repository;

import com.cjc.syt.model.hosp.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/4/1
 * Time: 10:01
 * To change this template use File | Settings | File Templates.
 **/
@Repository
public interface DepartmentRepository extends MongoRepository<Department,String> {

    Department getDepartmentByHoscodeAndDepcode(String hoscode, String depcode);
}
