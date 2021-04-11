package com.cjc.syt.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjc.syt.cmn.DictEeVo;
import com.cjc.syt.common.result.Result;
import com.cjc.syt.hosp.mapper.HospitalMapper;
import com.cjc.syt.hosp.mapper.HospitalSetMapper;
import com.cjc.syt.hosp.repository.HospitalRepository;
import com.cjc.syt.hosp.service.HospitalService;
import com.cjc.syt.model.hosp.BookingRule;
import com.cjc.syt.model.hosp.Department;
import com.cjc.syt.model.hosp.Hospital;
import com.cjc.syt.vo.hosp.HospitalQueryVo;
import com.cjc.syt.vo.hosp.HospitalSetQueryVo;
import com.cjc.yygh.cmn.client.DictFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/3/31
 * Time: 16:11
 * To change this template use File | Settings | File Templates.
 **/
@Service
@Slf4j
public class HospitalServiceImpl extends ServiceImpl<HospitalMapper, Hospital> implements HospitalService {

    @Resource
    private HospitalMapper hospitalMapper;

    @Autowired
    private HospitalRepository hospitalRepository;

    @Resource
    private DictFeignClient dictFeignClient;



    @Override
    public void save(Map<String, Object> parameterMap) {

        // 将参数mapper转换为对象
        String mapString = JSONObject.toJSONString(parameterMap);

        Hospital hospital = JSONObject.parseObject(mapString, Hospital.class);

        // 判断数据是否存在
        String hoscode = hospital.getHoscode();
        Hospital existHospital = hospitalRepository.getHospitalByHoscode(hoscode);
        // 存在，更新
        if(existHospital!=null){
            hospital.setStatus(existHospital.getStatus());
            hospital.setCreateTime(existHospital.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);

            hospitalRepository.save(hospital);
            log.info("更新成功, hospital"+hospital.toString());
        }else {
            // 不存在，保存
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            Hospital save = hospitalRepository.save(hospital);
            log.info("保存成功, hospital"+hospital.toString());

        }
    }

    /**
     * 根据hoscode查找医院
     * @param hoscode
     */
    @Override
    public Hospital getByHoscode(String hoscode) {
        Hospital hospitalByHoscode = hospitalRepository.getHospitalByHoscode(hoscode);
        return hospitalByHoscode;
    }

    @Override
    public Page<Hospital> selectHospPage(int page, int limit, HospitalQueryVo hospitalQueryVo) {
        //创建pageable对象
        Pageable pageable = PageRequest.of(page-1,limit);
        //创建条件匹配器
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        //hospitalSetQueryVo转换Hospital对象
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo,hospital);
        //创建对象
        Example<Hospital> example = Example.of(hospital,matcher);
        //调用方法实现查询
        Page<Hospital> pages = hospitalRepository.findAll(example, pageable);

        //获取查询list集合，遍历进行医院等级封装
        pages.getContent().stream().forEach(item -> {
            this.setHospitalHosType(item);
        });

        return pages;
    }

    @Override
    public Result getProvince() {
        long provinceId = 86;
        return dictFeignClient.findChildrenData(provinceId);
    }

    @Override
    public Result getCityListByProvinceId(long provinceId) {
        return dictFeignClient.findChildrenData(provinceId);
    }

    /**
     * 修改状态
     * @param status
     */
    @Override
    public void updateStatus(String id,Integer status) {
        Hospital hospital = hospitalRepository.findById(id).get();
        if (status == 1) {
            hospital.setStatus(0);
        } else {
            hospital.setStatus(1);
        }
        hospital.setUpdateTime(new Date());
        hospitalRepository.save(hospital);
    }

    @Override
    public Map<String,Object> getHospitalDetails(String id) {
        Hospital hospital = hospitalRepository.findById(id).get();
        // 设置医院类型
        // 设置医院详细地址
        Hospital hospitalWithData = setHospitalData(hospital);
        BookingRule bookingRule = hospital.getBookingRule();
        HashMap<String, Object> data = new HashMap<>();
        data.put("hospital",hospitalWithData);
        data.put("bookingRule",bookingRule);
        return data;
    }


    //获取查询list集合，遍历进行医院等级封装
    private Hospital setHospitalHosType(Hospital hospital) {
        //根据dictCode和value获取医院等级名称
        String hostypeString = dictFeignClient.getName("Hostype", hospital.getHostype());
        //查询省 市  地区
        String provinceString = dictFeignClient.getName(hospital.getProvinceCode());
        String cityString = dictFeignClient.getName(hospital.getCityCode());
        String districtString = dictFeignClient.getName(hospital.getDistrictCode());

        hospital.getParam().put("fullAddress",provinceString+cityString+districtString);
        hospital.getParam().put("hostypeString",hostypeString);
        return hospital;
    }

    /**
     * 封装数据字典
     * @param hospital
     * @return
     */
    private Hospital setHospitalData(Hospital hospital){
        // 封装医院类型
        String hostypeValue = hospital.getHostype();
        String hosTypeString = dictFeignClient.getName("Hostype", hostypeValue);
        hospital.setHostype(hostypeValue);

        // 封装省，市，区
        String province = dictFeignClient.getName(hospital.getProvinceCode());
        String city = dictFeignClient.getName(hospital.getCityCode());
        String district = dictFeignClient.getName(hospital.getDistrictCode());
        hospital.getParam().put("hostypeString",hosTypeString);
        hospital.getParam().put("fullAdress",province+" "+city+" "+district);
        return hospital;
    }



}
