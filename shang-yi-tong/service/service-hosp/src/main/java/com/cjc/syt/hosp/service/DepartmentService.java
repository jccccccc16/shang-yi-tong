package com.cjc.syt.hosp.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cjc.syt.model.hosp.Department;
import com.cjc.syt.model.hosp.HospitalSet;
import com.cjc.syt.vo.hosp.DepartmentQueryVo;
import com.cjc.syt.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface DepartmentService {

    /**
     * 保存科室到mongo
     * @param department
     * @return
     */
    public boolean save(Department department);

    Page<Department> findPageDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo);

    void removeDept(String hospcode, String depcode);

    /**
     *
     *
     * @return
     */
    List<DepartmentVo> getDepartmentTree(String hoscode);


    String getDepnameByHoscodeAndDepcode(String hoscode, String depcode);
}
