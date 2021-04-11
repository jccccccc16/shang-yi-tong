package com.cjc.syt.hosp.service;

import com.cjc.syt.model.hosp.Schedule;
import com.cjc.syt.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

/**
 * 排班
 */
public interface ScheduleService {
    /**
     * 保存schedule到mongo
     * @param schedule
     */
    void saveSchedule(Schedule schedule);

    Page<Schedule> findPageSchedule(int page, int limit, ScheduleQueryVo scheduleQueryVo);

    void remove(String hospcode, String hosScheduleId);
}
