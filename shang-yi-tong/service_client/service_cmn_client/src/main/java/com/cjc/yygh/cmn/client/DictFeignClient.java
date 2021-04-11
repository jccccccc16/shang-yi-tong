package com.cjc.yygh.cmn.client;

import com.cjc.syt.common.result.Result;
import com.cjc.syt.model.cmn.Dict;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/4/7
 * Time: 20:06
 * To change this template use File | Settings | File Templates.
 **/
@FeignClient("service-cmn")
public interface DictFeignClient {

    /**
     * 根据dictCode和value查询
     * @param dictCode
     * @param value
     * @return
     */
    @GetMapping("/admin/cmn/dict/getName/{dictCode}/{value}")
    public String getName(@PathVariable("dictCode") String dictCode,
                          @PathVariable("value") String value);

    /**
     * 根据value查询
     * @param value
     * @return
     */
    @GetMapping("/admin/cmn/dict/getName/{value}")
    public String getName(@PathVariable("value") String value);


    @GetMapping("/admin/cmn/dict/find/children/data/{id}")
    public Result findChildrenData(@PathVariable("id")Long id);

}
