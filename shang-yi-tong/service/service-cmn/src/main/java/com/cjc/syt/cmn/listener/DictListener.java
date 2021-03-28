package com.cjc.syt.cmn.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjc.syt.cmn.DictEeVo;
import com.cjc.syt.cmn.mapper.DictMapper;
import com.cjc.syt.model.cmn.Dict;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

/**
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/3/28
 * Time: 9:55
 * To change this template use File | Settings | File Templates.
 **/
@Slf4j
public class DictListener extends AnalysisEventListener<DictEeVo> {

    private int count = 0;

    private DictMapper dictMapper;

    public DictListener(DictMapper dictMapper){
        this.dictMapper = dictMapper;
    }

    /**
     * 读取一行都会调用该方法
     * 读取数据后如何做
     * @param dictEeVo
     * @param analysisContext
     */
    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        count = count + 1;
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictEeVo,dict);
        dict.setIsDeleted(0);
        dictMapper.insert(dict);
        log.info("插入第 "+ count +" 行数据 dict："+dict.toString());
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
