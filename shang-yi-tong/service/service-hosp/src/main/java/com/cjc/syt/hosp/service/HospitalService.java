package com.cjc.syt.hosp.service;

import com.cjc.syt.cmn.DictEeVo;
import com.cjc.syt.common.result.Result;
import com.cjc.syt.model.hosp.Hospital;
import com.cjc.syt.vo.hosp.HospitalQueryVo;
import com.cjc.syt.vo.hosp.HospitalSetQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface HospitalService {
    void save(Map<String, Object> parameterMap);

    Hospital getByHoscode(String hoscode);

    /**
     * 获取医院列表（分页）
     * @param page
     * @param limit
     * @param hospitalSetQueryVo
     * @return
     */
    Page<Hospital> selectHospPage(int page, int limit, HospitalQueryVo hospitalSetQueryVo);


    /**
     * 获取省份列表
     */
    Result getProvince();

    /**
     * 获取市区
     * @return
     */
    Result getCityListByProvinceId(long provinceId);

    /**
     * 修改状态
     * @param status
     */
    void updateStatus(String id,Integer status);

    /**
     * 获取医院详细信息
     * @param id
     * @return
     */
    Map<String,Object>  getHospitalDetails(String id);
}
