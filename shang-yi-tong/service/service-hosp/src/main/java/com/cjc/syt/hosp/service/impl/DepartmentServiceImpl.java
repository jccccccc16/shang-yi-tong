package com.cjc.syt.hosp.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjc.syt.hosp.mapper.DepartmentMapper;
import com.cjc.syt.hosp.repository.DepartmentRepository;
import com.cjc.syt.hosp.service.DepartmentService;
import com.cjc.syt.model.hosp.Department;
import com.cjc.syt.vo.hosp.DepartmentQueryVo;
import com.cjc.syt.vo.hosp.DepartmentVo;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public List<DepartmentVo> getDepartmentTree(String hoscode) {
        // 查询出所有的部门
        Department department = new Department();
        department.setHoscode(hoscode);
        Example<Department> example = Example.of(department);
        List<Department> departmentList = departmentRepository.findAll(example);

        // 键为bigcode，以bigcode为分组
        Map<String, List<Department>> collect = departmentList.stream().collect(Collectors.groupingBy(Department::getBigcode));


        // 定义返回结果
        ArrayList<DepartmentVo> result = new ArrayList<>();
        for(Map.Entry<String,List<Department>> entry:collect.entrySet()){

            String bigcode = entry.getKey();

            List<Department> childrenList = entry.getValue();

            // 封装为一个大科室
            DepartmentVo parentDepartment = new DepartmentVo();
            parentDepartment.setDepcode(bigcode);
            parentDepartment.setDepname(childrenList.get(0).getBigname());


            // 封装小科室
            ArrayList<DepartmentVo> childrenDepartments = new ArrayList<DepartmentVo>();
            for (Department children : childrenList) {

                DepartmentVo childrenVo = new DepartmentVo();
                childrenVo.setDepname(children.getDepname());
                childrenVo.setDepcode(children.getDepcode());
                childrenDepartments.add(childrenVo);
            }

            parentDepartment.setChildren(childrenDepartments);
            result.add(parentDepartment);
        }

        return result;
    }

    @Override
    public String getDepnameByHoscodeAndDepcode(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        return department.getDepname();
    }




}
