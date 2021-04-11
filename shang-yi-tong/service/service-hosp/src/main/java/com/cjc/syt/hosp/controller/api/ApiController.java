package com.cjc.syt.hosp.controller.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjc.syt.common.exception.YyghException;
import com.cjc.syt.common.helper.HttpRequestHelper;
import com.cjc.syt.common.result.Result;
import com.cjc.syt.common.result.ResultCodeEnum;
import com.cjc.syt.common.utils.HospitalUtils;
import com.cjc.syt.common.utils.MD5;
import com.cjc.syt.common.utils.ParameterWrapper;
import com.cjc.syt.hosp.service.DepartmentService;
import com.cjc.syt.hosp.service.HospitalService;
import com.cjc.syt.hosp.service.IHospitalSetService;
import com.cjc.syt.hosp.service.ScheduleService;
import com.cjc.syt.model.hosp.Department;
import com.cjc.syt.model.hosp.Hospital;
import com.cjc.syt.model.hosp.Schedule;
import com.cjc.syt.vo.hosp.DepartmentQueryVo;
import com.cjc.syt.vo.hosp.ScheduleQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 供医院系统调用的接口
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/3/31
 * Time: 16:12
 * To change this template use File | Settings | File Templates.
 **/
@RestController
@RequestMapping("/api/hosp")
@Slf4j
public class ApiController {

    @Autowired
    private HospitalService  hospitalService;

    @Autowired
    private IHospitalSetService hospitalSetService;

    @Autowired
    private  DepartmentService departmentService;

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/saveSchedule")
    public Result saveSchedule(HttpServletRequest request){
        ParameterWrapper parameterWrapper = ParameterWrapper.build(request);
        String hospcode = parameterWrapper.getHospcode();
        String signKeySource = hospitalSetService.getSignKey(hospcode);
        String signKeyTarget = parameterWrapper.getSignKey();
        //parameterWrapper.isSignKeyValidated(signKeyTarget,signKeySource);
        Schedule schedule = parameterWrapper.toObject(Schedule.class);
        scheduleService.saveSchedule(schedule);
        return Result.ok();
    }

    @PostMapping("/schedule/remove")
    public Result removeSchedule(HttpServletRequest request){
        ParameterWrapper parameterWrapper = ParameterWrapper.build(request);
        String signKeyTarget = parameterWrapper.getSignKey();
        String hospcode = parameterWrapper.getHospcode();
        String signKeySource = hospitalSetService.getSignKey(hospcode);
        parameterWrapper.isSignKeyValidated(signKeyTarget,signKeySource);
        String hosScheduleId = parameterWrapper.getParameter("hosScheduleId");
        scheduleService.remove(hospcode,hosScheduleId);
        return Result.ok();
    }

    @PostMapping("/schedule/list")
    public Result findSchedule(HttpServletRequest request){
        ParameterWrapper parameterWrapper = ParameterWrapper.build(request);
        String hospcode = parameterWrapper.getHospcode();
        int page = (StringUtils.isEmpty(parameterWrapper.getParameter("page")))?1:Integer.parseInt(parameterWrapper.getParameter("page"));
        int limit = (StringUtils.isEmpty(parameterWrapper.getParameter("limit")))?1:Integer.parseInt(parameterWrapper.getParameter("limit"));
        String signKeyTarget = parameterWrapper.getSignKey();
        String signKeySource = hospitalSetService.getSignKey(hospcode);
        // 校验签名
        parameterWrapper.isSignKeyValidated(signKeyTarget,signKeySource);
        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        Page<Schedule> pageSchedule = scheduleService.findPageSchedule(page, limit, scheduleQueryVo);
        return Result.ok(pageSchedule);
    }

    /**
     * 上传医院接口
     * @param request
     * @return
     */
    @PostMapping("/saveHospital")
    public Result saveHospital(HttpServletRequest request){
        Map<String, String[]> rquestMap = request.getParameterMap();
        Map<String, Object> parameterMap = HttpRequestHelper.switchMap(rquestMap);
        // 判断该医院的数据签名是否一致
        // 获取签名
        String signMD5 = (String) parameterMap.get("sign");

        // 根据医院编号查询数据库
        String hoscode = (String) parameterMap.get("hoscode");

        String signKeyFromPersis =  hospitalSetService.getSignKey(hoscode);

        String signKeyFromPersisMD5 = MD5.encrypt(signKeyFromPersis);

        // 判断签名是否一致
        if(!signKeyFromPersisMD5.equals(signMD5)){
            // 抛出签名不一致错误
            log.error("上传医院失败，签名不一致");
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        // 为了数据传输我们将图片进行了base64的编码
        // 传输过程中"+"会转换为" "空格，所以我们需要转换回来
        String logoData = (String) parameterMap.get("logoData");
        logoData = logoData.replace(" ","+");
        parameterMap.put("logoData",logoData);

        hospitalService.save(parameterMap);
        log.info("上传医院成功!");
        return Result.ok();
    }

    /**
     * 数据示例
     * [
     * {"hoscode":"1000_0","depcode":"200050923","depname":"门诊部核酸检测门诊(东院)","intro":"门诊部核酸检测门诊(东院)","bigcode":"44f162029abb45f9ff0a5f743da0650d","bigname":"全部科室"},
     * @param request
     * @return
     */
    @PostMapping("/saveDepartment")
    public Result saveDepartment(HttpServletRequest request){

        ParameterWrapper parameterWrapper = ParameterWrapper.build(request);
        Department department = parameterWrapper.toObject(Department.class);
//        String hoscode = parameterWrapper.getParameter("hoscode");
//        String signMD5 = parameterWrapper.getParameter("sign");
        String hospcode = parameterWrapper.getHospcode();
        String signMD5 = parameterWrapper.getSignKey();
        String signKey = hospitalSetService.getSignKey(hospcode);
        // 判断签名是否一致
        parameterWrapper.isSignKeyValidated(signMD5,signKey);
        departmentService.save(department);
        log.info("上传部门成功!");
        return Result.ok();
    }

    /**
     * 查询科室接口
     * @param request
     * @return
     */
    @PostMapping("/department/list")
    public Result findDepartment(HttpServletRequest request){
        ParameterWrapper parameterWrapper = ParameterWrapper.build(request);

        String hospcode = parameterWrapper.getHospcode();

        int page = (StringUtils.isEmpty(parameterWrapper.getParameter("page")))?1:Integer.parseInt(parameterWrapper.getParameter("page"));
        int limit = (StringUtils.isEmpty(parameterWrapper.getParameter("limit")))?1:Integer.parseInt(parameterWrapper.getParameter("limit"));
        String signKeyTarget = parameterWrapper.getSignKey();
        String signKeySource = hospitalSetService.getSignKey(hospcode);
        // 校验签名
        parameterWrapper.isSignKeyValidated(signKeyTarget,signKeySource);
        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hospcode);
        Page<Department> pageModel = departmentService.findPageDepartment(page,limit,departmentQueryVo);
        log.info("查找科室");
        return Result.ok(pageModel);
    }


    @PostMapping("/department/remove")
    public Result removeDepartment(HttpServletRequest request){

        ParameterWrapper parameterWrapper = ParameterWrapper.build(request);
        String hospcode = parameterWrapper.getHospcode();
        String depcode = parameterWrapper.getParameter("depcode");
        departmentService.removeDept(hospcode,depcode);
        log.info("删除 hospcode："+hospcode+" , depcode："+depcode + " 科室");
        return Result.ok();


    }




    @PostMapping("/hospital/show")
    public Result getHospital(HttpServletRequest request){
        Map<String, Object> parameterMap = HospitalUtils.getParameterMap(request);
        String targetSignKey = (String) parameterMap.get("sign");
        String hoscode = (String) parameterMap.get("hoscode");
        String signKeySource = hospitalSetService.getSignKey(hoscode);

        // 判断签名是否一致，否则抛出异常
        HospitalUtils.isSignKeyValidated(targetSignKey, signKeySource);

        // 执行操作
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        // 返回result
        return Result.ok(hospital);

    }




}
