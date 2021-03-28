package com.cjc.syt.hosp.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjc.syt.hosp.mapper.HospitalSetMapper;
import com.cjc.syt.model.hosp.HospitalSet;
import com.cjc.syt.hosp.service.IHospitalSetService;
import org.springframework.stereotype.Service;

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
}