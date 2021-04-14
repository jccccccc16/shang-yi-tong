package com.cjc.syt.hosp.controller;

import com.cjc.syt.common.result.Result;
import com.cjc.syt.hosp.service.DepartmentService;
import com.cjc.syt.vo.hosp.DepartmentVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/4/11
 * Time: 9:32
 * To change this template use File | Settings | File Templates.
 **/
@RestController
@RequestMapping("/admin/hosp/department")
//@CrossOrigin
@Slf4j
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/get/department/{hoscode}")
    public Result getDepartment(@PathVariable("hoscode")String hoscode){

        List<DepartmentVo> departmentTree = departmentService.getDepartmentTree(hoscode);
        log.info("查找到数据为："+departmentTree.toString());
        return Result.ok(departmentTree);
    }

}



