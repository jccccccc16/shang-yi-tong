package com.cjc.syt.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjc.syt.cmn.DictEeVo;
import com.cjc.syt.cmn.listener.DictListener;
import com.cjc.syt.cmn.mapper.DictMapper;
import com.cjc.syt.cmn.service.DictService;
import com.cjc.syt.model.cmn.Dict;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/3/27
 * Time: 10:03
 * To change this template use File | Settings | File Templates.
 **/
@Service
@Slf4j
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Resource
    private DictMapper dictMapper;


    @Override
    @Cacheable(value="dict",keyGenerator = "keyGenerator")
    public List<Dict> findChildrenData(Long id) {
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("parent_id", id);
        List<Dict> dictList = baseMapper.selectList(dictQueryWrapper);

        // 设置对象中的hasChildren字段
        for (Dict dict : dictList) {
            Long dictId = dict.getId();
            boolean hasChildren = hasChildren(dictId);
            dict.setHasChildren(hasChildren);
        }

        return dictList;
    }

    /**
     * 导出数据
     */
    @Override
    public void exportDict(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String filename = URLEncoder.encode("数据字典", "UTF-8");
        // Content-disposition 以下载的方式打开
        response.setHeader("Content-disposition","attachment;filename="+filename+".xlsx");
        // 查询数据
        List<Dict> dictList = baseMapper.selectList(null);
        log.info("查询的数据为：dictList="+dictList);
        // 封装为DICTEevo
        List<DictEeVo> dictEeVoList = new ArrayList<>();
        for (Dict dict : dictList) {
            DictEeVo dictEeVo = new DictEeVo();
            BeanUtils.copyProperties(dict,dictEeVo);
            dictEeVoList.add(dictEeVo);
        }
        log.info("转换为dictEeVo dictEeVoList = "+dictEeVoList);
        // 写出excel数据
        EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("dict").doWrite(dictEeVoList);
    }


    /**
     * 导入数据
     * @param file
     */
    @Override
    @CacheEvict(value = "dict",allEntries = true)
    public void importData(MultipartFile file) throws IOException {

        EasyExcel.read(file.getInputStream(),DictEeVo.class,new DictListener(dictMapper)).sheet().doRead();;

    }

    @Override
    public String getDictName(String dictCode, String value) {
        // 如果dictcode为空，那么根据value查询
        // 如果dictcode不为空，那么根据value和dictcode查询

        if(StringUtils.isEmpty(dictCode)){
            QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
            dictQueryWrapper.eq("value", value);
            Dict dict = baseMapper.selectOne(dictQueryWrapper);
            return dict.getName();
        }else{
            // 根据dictCode查出父节点的id，然后更具父节点id以及value查询数据字典的name
            QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
            dictQueryWrapper.eq("dict_code", dictCode);
            // 查出父节点
            Dict parentDict = baseMapper.selectOne(dictQueryWrapper);
            Long parent_id = parentDict.getId();
            // 根据父节点id以及value查询
            QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("value",value).eq("parent_id",parent_id);
            Dict dict = baseMapper.selectOne(queryWrapper);
            return dict.getName();
        }
    }

    @Override
    public List<Dict> findByDictCode(String dictCode) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dict_code",dictCode);
        Dict dict = dictMapper.selectOne(queryWrapper);
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("parent_id",dict.getId());
        List<Dict> dictList = dictMapper.selectList(dictQueryWrapper);
        return dictList;
    }

    /**
     * 是否有子数据
     * @param id
     * @return
     */
    private boolean hasChildren(Long id){

        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(wrapper);
        return count > 0;


    }
}
