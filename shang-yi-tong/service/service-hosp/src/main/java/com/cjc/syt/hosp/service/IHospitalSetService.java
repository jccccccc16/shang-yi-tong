package com.cjc.syt.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjc.syt.model.hosp.Hospital;
import com.cjc.syt.model.hosp.HospitalSet;


/**
 * <p>
 * 医院设置表 服务类
 * </p>
 *
 * @author jobob
 * @since 2021-03-22
 */
public interface IHospitalSetService extends IService<HospitalSet> {

    String getSignKey(String hoscode);


}
