package com.cjc.syt.hosp.repository;

import com.cjc.syt.model.hosp.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/4/4
 * Time: 11:11
 * To change this template use File | Settings | File Templates.
 **/
@Repository
public interface ScheduleRepository extends MongoRepository<Schedule,String> {
    Schedule getScheduleByHoscodeAndHosScheduleId(String hospcode, String hosScheduleId);
}
