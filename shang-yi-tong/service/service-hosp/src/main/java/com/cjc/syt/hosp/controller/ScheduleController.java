package com.cjc.syt.hosp.controller;

import com.cjc.syt.common.result.Result;
import com.cjc.syt.hosp.service.ScheduleService;
import com.cjc.syt.model.hosp.Schedule;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/4/11
 * Time: 19:55
 * To change this template use File | Settings | File Templates.
 **/
@RestController
@RequestMapping("/admin/hosp/schedule")
//@CrossOrigin
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation(value = "获取排班规则")
    @GetMapping("/get/schedule/rule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getScheduleRule(@PathVariable("page")long page,
                                  @PathVariable("limit")long limit,
                                  @PathVariable("hoscode") String hoscode,
                                  @PathVariable("depcode")String depcode){
        Map<String,Object> map = scheduleService.getScheduleRule(page,limit,hoscode,depcode);
        return Result.ok(map);
    }


    @ApiOperation(value = "查询排班详情")
    @GetMapping("/get/schedule/details/{hoscode}/{depcode}/{workDate}")
   public Result getScheduleDetails(@PathVariable("hoscode")String hoscode,
                                    @PathVariable("depcode")String depcodem,
                                    @PathVariable("workDate")String workDate){
        List<Schedule> scheduleList = scheduleService.getDetailSchedule(hoscode,depcodem,workDate);
        return Result.ok(scheduleList);
    }
}
