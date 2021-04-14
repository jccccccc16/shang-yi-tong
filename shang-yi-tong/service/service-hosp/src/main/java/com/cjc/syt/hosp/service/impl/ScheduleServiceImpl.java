package com.cjc.syt.hosp.service.impl;

import com.cjc.syt.hosp.repository.ScheduleRepository;
import com.cjc.syt.hosp.service.DepartmentService;
import com.cjc.syt.hosp.service.HospitalService;
import com.cjc.syt.hosp.service.ScheduleService;
import com.cjc.syt.model.hosp.Department;
import com.cjc.syt.model.hosp.Hospital;
import com.cjc.syt.model.hosp.Schedule;
import com.cjc.syt.vo.hosp.BookingScheduleRuleVo;
import com.cjc.syt.vo.hosp.ScheduleQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DepartmentService departmentService;

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

    /**
     * 根据日期分组，获取各个分组的已经预约的数量
     *
     *
     *
     *
     * @param page
     * @param limit
     * @param hoscode
     * @param depcode
     * @return
     */
    @Override
    public Map<String, Object> getScheduleRule(long page, long limit, String hoscode, String depcode) {
        // 封装查询条件
        Criteria criteria = Criteria.where("hoscode").is(hoscode)
                .and("depcode").is(depcode);
        // 聚合分组
        Aggregation aggregation = Aggregation.newAggregation(
              Aggregation.match(criteria),
              Aggregation.group("workDate")
                      .first("workDate").as("workDate")
                      // 计算号源数量，reservedNumber为剩余量
                .count().as("docCount")
                .sum("reservedNumber").as("reservedNumber")
                .sum("availableNumber").as("availableNumber"),
                Aggregation.sort(Sort.Direction.DESC,"workDate"),
                // 分页
                Aggregation.skip((page-1)*limit),
                Aggregation.limit(limit)
        );
        AggregationResults<BookingScheduleRuleVo> aggregationResults = mongoTemplate.aggregate(aggregation, "Schedule", BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> bookingScheduleRuleVoResults = aggregationResults.getMappedResults();
        // 分组查询的总记录数
        Aggregation totalAgg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
        );
        int total = mongoTemplate.aggregate(totalAgg, "Schedule", BookingScheduleRuleVo.class).getMappedResults().size();

        // 获取日期对应的星期
        for(BookingScheduleRuleVo bookingScheduleRuleVo:bookingScheduleRuleVoResults){
            Date workDate = bookingScheduleRuleVo.getWorkDate();
            String dayOfWeek = this.getDayOfWeek(new DateTime(workDate));
            bookingScheduleRuleVo.setDayOfWeek(dayOfWeek);
            log.info("----------------------dayOfWeek"+dayOfWeek);
        }

        HashMap<String, Object> result = new HashMap<>();
        result.put("bookingScheduleRuleList",bookingScheduleRuleVoResults);
        result.put("total",total);

        // 获取医院名称
        String hosname = hospitalService.getByHoscode(hoscode).getHosname();
        Map<String,String> baseMap = new HashMap<>();
        baseMap.put("hsoname",hosname);
        result.put("baseMap",baseMap);
        return result;
    }

    @Override
    public List<Schedule> getDetailSchedule(String hoscode, String depcodem, String workDate) {
        List<Schedule> scheduleList = scheduleRepository.findScheduleByHoscodeAndDepcodeAndWorkDate(hoscode, depcodem, new DateTime(workDate).toDate());
        // 设置一些数据
        scheduleList.stream().forEach(item->{
            this.packageSchedule(item);
        });

        return scheduleList;

    }

    private void packageSchedule(Schedule item) {
        // 设置医院名称
        String hosname = hospitalService.getByHoscode(item.getHoscode()).getHosname();
        item.getParam().put("hosname",hosname);
        // 设置科室名称
        String depname = departmentService.getDepnameByHoscodeAndDepcode(item.getHoscode(),item.getDepcode());
        item.getParam().put("depname",depname);

        item.getParam().put("dayOfWeek", this.getDayOfWeek(new DateTime(item.getWorkDate())));

    }

    /**
     * 根据日期获取周几数据
     * @param dateTime
     * @return
     */
    private String getDayOfWeek(DateTime dateTime) {
        String dayOfWeek = "";
        switch (dateTime.getDayOfWeek()) {
            case DateTimeConstants.SUNDAY:
                dayOfWeek = "周日";
                break;
            case DateTimeConstants.MONDAY:
                dayOfWeek = "周一";
                break;
            case DateTimeConstants.TUESDAY:
                dayOfWeek = "周二";
                break;
            case DateTimeConstants.WEDNESDAY:
                dayOfWeek = "周三";
                break;
            case DateTimeConstants.THURSDAY:
                dayOfWeek = "周四";
                break;
            case DateTimeConstants.FRIDAY:
                dayOfWeek = "周五";
                break;
            case DateTimeConstants.SATURDAY:
                dayOfWeek = "周六";
            default:
                break;
        }
        return dayOfWeek;
    }
}
