package com.cjc.syt.cmn.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjc.syt.cmn.DictEeVo;
import com.cjc.syt.cmn.service.DictService;
import com.cjc.syt.common.result.Result;
import com.cjc.syt.model.cmn.Dict;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/3/27
 * Time: 10:04
 * To change this template use File | Settings | File Templates.
 **/
@Slf4j
@RestController
@RequestMapping("/admin/cmn/dict")
//@CrossOrigin
public class DictController {

    @Resource
    private DictService dictService;

    @ApiOperation(value = "根据数据id查询子数据列表")
    @GetMapping("/find/children/data/{id}")
    public Result findChildrenData(@PathVariable("id")Long id){
        List<Dict> dictList = dictService.findChildrenData(id);
        return Result.ok(dictList);

    }

    @ApiOperation(value = "根据dictCode获取下级节点的")
    @GetMapping("/findByDictCode/{dictCode}")
    public Result findByDictCode(@PathVariable String dictCode){
        List<Dict> list = dictService.findByDictCode(dictCode);
        return Result.ok(list);
    }
    /**
     * 导出数据字典接口
     * @return
     */
    @GetMapping("/exportData")
    public Result exportDict(HttpServletResponse response) throws IOException {

        try {
            dictService.exportDict(response);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
        return Result.ok();
    }


    @ApiOperation(value = "导入数据")
    @PostMapping("/importData")
    public Result importData(MultipartFile file){
        try {
            dictService.importData(file);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
        return Result.ok();
    }

    /**
     * 根据dictCode和value查询
     * @param dictCode
     * @param value
     * @return
     */
    @GetMapping("/getName/{dictCode}/{value}")
    public String getName(@PathVariable String dictCode,
                          @PathVariable String value){
        String dictName = dictService.getDictName(dictCode,value);
        return dictName;
    }

    /**
     * 根据value查询
     * @param value
     * @return
     */
    @GetMapping("/getName/{value}")
    public String getName(@PathVariable String value){
        String dictName = dictService.getDictName("",value);
        return dictName;
    }






}
