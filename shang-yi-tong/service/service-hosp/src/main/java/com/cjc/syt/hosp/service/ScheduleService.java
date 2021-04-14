package com.cjc.syt.hosp.service;

import com.cjc.syt.model.hosp.Schedule;
import com.cjc.syt.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

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

    /**
     * 获取排班规则，
     * @param page
     * @param limit
     * @param hoscode
     * @param depcode
     * @return
     */
    Map<String, Object> getScheduleRule(long page, long limit, String hoscode, String depcode);

    List<Schedule> getDetailSchedule(String hoscode, String depcodem, String workDate);
}
