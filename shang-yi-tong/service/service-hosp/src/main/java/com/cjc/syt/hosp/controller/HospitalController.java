package com.cjc.syt.hosp.controller;

import com.cjc.syt.common.result.Result;
import com.cjc.syt.hosp.service.HospitalService;
import com.cjc.syt.model.hosp.Hospital;
import com.cjc.syt.vo.hosp.HospitalQueryVo;
import com.cjc.syt.vo.hosp.HospitalSetQueryVo;
import com.cjc.yygh.cmn.client.DictFeignClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ConditionalOnEnabledResourceChain;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/4/6
 * Time: 16:31
 * To change this template use File | Settings | File Templates.
 **/
@RestController
@RequestMapping("/admin/hosp/hospital")
//@CrossOrigin
@Slf4j
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;


    /**
     * 查询医院了列表，带分页条件查询
     */
    @PostMapping("/list/{page}/{limit}")
    @ApiOperation(value = "获取医院列表")
    public Result getList(@PathVariable("page") int page,
                          @PathVariable("limit") int limit
    , HospitalQueryVo hospitalSetQueryVo){
        Page<Hospital> pageModel = hospitalService.selectHospPage(page,limit,hospitalSetQueryVo);
        return Result.ok(pageModel);
    }

    /**
     * 获取省份
     * @return
     */
    @GetMapping("/get/province/list")
    @ApiOperation(value = "获取省份")
    public Result getProvinceList(){
        return hospitalService.getProvince();
    }

    @GetMapping("/get/city/list/by/{provinceId}")
    @ApiOperation(value = "根据省份id获取对应市区列表")
    public Result getCityList(@PathVariable("provinceId") long provinceId){
        return hospitalService.getCityListByProvinceId(provinceId);
    }

    @GetMapping("/update/hospital/{status}/by/{id}")
    public Result updateHospital(@PathVariable("status") Integer status,
                                 @PathVariable("id")String id){
        hospitalService.updateStatus(id,status);
        return Result.ok();
    }

    @GetMapping("/get/hospital/details/by/{id}")
    public Result getHospitalDetails(@PathVariable("id")String id){
        Map<String, Object> hospitalDetails = hospitalService.getHospitalDetails(id);

        log.info("查询成功: "+hospitalDetails.toString());
        return Result.ok(hospitalDetails);
    }


}
