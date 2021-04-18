package com.cjc.syt.hosp.repository;

import com.cjc.syt.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/3/31
 * Time: 16:09
 * To change this template use File | Settings | File Templates.
 **/
@Repository
public interface HospitalRepository extends MongoRepository<Hospital,String> {
    //判断该医院是否存在
    Hospital getHospitalByHoscode(String hoscode);

    /**
     * 模糊查询
     * @param hosname
     * @return
     */
    List<Hospital> findHospitalByHosnameLike(String hosname);
}
