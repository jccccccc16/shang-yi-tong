package com.cjc.syt.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjc.syt.hosp.mapper.DepartmentMapper;
import com.cjc.syt.hosp.repository.DepartmentRepository;
import com.cjc.syt.hosp.service.DepartmentService;
import com.cjc.syt.model.hosp.Department;
import com.cjc.syt.vo.hosp.DepartmentQueryVo;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/4/1
 * Time: 10:02
 * To change this template use File | Settings | File Templates.
 **/
@Service
@Slf4j
public class DepartmentServiceImpl  implements DepartmentService  {

    @Autowired
    private DepartmentRepository departmentRepository;
    /**
     * 保存科室
     *
     */
    public boolean save(Department department) {
        try{

            Department departmentFromMongo = departmentRepository.getDepartmentByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());
            if(departmentFromMongo!=null){
                departmentFromMongo.setUpdateTime(new Date());
                departmentFromMongo.setIsDeleted(0);
                departmentRepository.save(departmentFromMongo);
            }else{
                department.setCreateTime(new Date());
                department.setUpdateTime(new Date());
                department.setIsDeleted(0);
                departmentRepository.save(department);
            }

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public Page<Department> findPageDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo) {
        // 0为第一页
        PageRequest pageRequest = PageRequest.of(page-1, limit);
        // 构建规则
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        // 封装参数与规则
        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo,department);
        Example<Department> example = Example.of(department, exampleMatcher);
        Page<Department> all = departmentRepository.findAll(example, pageRequest);
        return all;
    }

    @Override
    public void removeDept(String hospcode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hospcode, depcode);
        if(department!=null) {
            departmentRepository.deleteById(department.getId());
            log.info("删除id为 "+department.getId()+" 的科室");
            log.info("department: "+department.toString());
        }

    }
}
