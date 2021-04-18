package com.cjc.syt.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjc.syt.cmn.DictEeVo;
import com.cjc.syt.common.result.Result;
import com.cjc.syt.model.cmn.Dict;
import org.apache.http.HttpResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/3/27
 * Time: 10:02
 * To change this template use File | Settings | File Templates.
 **/
public interface DictService extends IService<Dict> {
    public List<Dict> findChildrenData(Long id);

    public void exportDict(HttpServletResponse response) throws IOException;

    void importData(MultipartFile file) throws IOException;

    String getDictName(String dictCode, String value);

    List<Dict> findByDictCode(String dictCode);

    /**
     * 根据父id查询id
     * @param province_id
     * @return
     */

}
