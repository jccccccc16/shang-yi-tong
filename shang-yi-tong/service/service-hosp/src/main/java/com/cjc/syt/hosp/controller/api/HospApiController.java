package com.cjc.syt.hosp.controller.api;

import com.cjc.syt.common.result.Result;
import com.cjc.syt.hosp.service.DepartmentService;
import com.cjc.syt.hosp.service.HospitalService;
import com.cjc.syt.hosp.service.ScheduleService;
import com.cjc.syt.model.hosp.Hospital;
import com.cjc.syt.vo.hosp.DepartmentVo;
import com.cjc.syt.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 供用户系统用
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/4/14
 * Time: 19:54
 * To change this template use File | Settings | File Templates.
 **/
@Slf4j
@RestController
@RequestMapping("/api/hosp/hospital")
public class HospApiController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation(value = "医院查询列表")
    @GetMapping("/list/{page}/{limit}")
    public Result findHospital(@PathVariable("page") Integer page,
                               @PathVariable("limit")  Integer limit,
                               HospitalQueryVo hospitalQueryVo){
        Page<Hospital> hospitals = hospitalService.selectHospPage(page, limit, hospitalQueryVo);
        return Result.ok(hospitals);
    }

    @ApiOperation(value = "根据医院名称模糊查询")
    @GetMapping("/find/hospital/by/hosname/{hosname}")
    public Result findByHosname(@PathVariable("hosname") String hosname){
        List<Hospital> hospitalList =  hospitalService.findByHosname(hosname);
        return Result.ok(hospitalList);
    }

    /**
     * DepartmentVo： 大科室包含小科室
     * @param hoscode
     * @return
     */
    @ApiOperation(value = "根据hoscode获取科室")
    @GetMapping("/get/department/{hoscode}")
    public Result getDepartment(@PathVariable("hoscode")String hoscode){

        List<DepartmentVo> departmentTree = departmentService.getDepartmentTree(hoscode);
        log.info("查找到数据为："+departmentTree.toString());
        return Result.ok(departmentTree);
    }

    @ApiOperation(value = "获取排班规则")
    @GetMapping("/get/schedule/rule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getScheduleRule(@PathVariable("page")long page,
                                  @PathVariable("limit")long limit,
                                  @PathVariable("hoscode") String hoscode,
                                  @PathVariable("depcode")String depcode){
        Map<String,Object> map = scheduleService.getScheduleRule(page,limit,hoscode,depcode);
        return Result.ok(map);
    }

    @ApiOperation(value = "根据hoscode查询医院详情")
    @GetMapping("/details/by/{hoscode}")
    public Result getHospitalDetail(@PathVariable("hoscode")String hoscode){
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        return Result.ok(hospital);
    }

    @ApiOperation(value = "获取医院预约挂号规则详情以及医院详情")
    @GetMapping("/get/bookingRule/{hoscode}")
    public Result getRuleDetails(@PathVariable("hoscode")String hoscode){
        return Result.ok(hospitalService.item(hoscode));
    }

}
