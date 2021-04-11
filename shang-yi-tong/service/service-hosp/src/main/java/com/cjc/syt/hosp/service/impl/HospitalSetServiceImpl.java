package com.cjc.syt.hosp.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjc.syt.hosp.mapper.HospitalSetMapper;
import com.cjc.syt.hosp.repository.HospitalRepository;
import com.cjc.syt.model.hosp.Hospital;
import com.cjc.syt.model.hosp.HospitalSet;
import com.cjc.syt.hosp.service.IHospitalSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 医院设置表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2021-03-22
 */
@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements IHospitalSetService {

    @Resource
    private HospitalSetMapper hospitalSetMapper;

    @Override
    public String getSignKey(String hoscode) {
        QueryWrapper<HospitalSet> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("hoscode",hoscode);

        return hospitalSetMapper.selectOne(queryWrapper).getSignKey();
    }


}
