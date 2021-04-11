package com.cjc.syt.hosp.service.impl;

import com.cjc.syt.hosp.repository.ScheduleRepository;
import com.cjc.syt.hosp.service.ScheduleService;
import com.cjc.syt.model.hosp.Department;
import com.cjc.syt.model.hosp.Schedule;
import com.cjc.syt.vo.hosp.ScheduleQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/4/4
 * Time: 11:13
 * To change this template use File | Settings | File Templates.
 **/
@Service
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Override
    public void saveSchedule(Schedule schedule) {
        scheduleRepository.save(schedule);
        log.info("保存成功 "+schedule);
    }

    @Override
    public Page<Schedule> findPageSchedule(int page, int limit, ScheduleQueryVo scheduleQueryVo) {
        // 0为第一页
        PageRequest pageRequest = PageRequest.of(page-1, limit);
        // 构建规则
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        // 封装参数与规则
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleQueryVo,schedule);
        Example<Schedule> example = Example.of(schedule, exampleMatcher);
        return scheduleRepository.findAll(example, pageRequest);

    }

    @Override
    public void remove(String hospcode, String hosScheduleId) {
        //根据医院编号和排班编号查询信息
        Schedule schedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hospcode, hosScheduleId);
        if(schedule != null) {
            scheduleRepository.deleteById(schedule.getId());
        }
    }
}
