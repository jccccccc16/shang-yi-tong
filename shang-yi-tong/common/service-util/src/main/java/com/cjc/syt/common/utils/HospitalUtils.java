package com.cjc.syt.common.utils;

import com.cjc.syt.common.exception.YyghException;
import com.cjc.syt.common.helper.HttpRequestHelper;
import com.cjc.syt.common.result.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/3/31
 * Time: 21:43
 * To change this template use File | Settings | File Templates.
 **/
@Slf4j
public class HospitalUtils {



    public static Map<String, Object> getParameterMap(HttpServletRequest request){
        Map<String, String[]> rquestMap = request.getParameterMap();
        return HttpRequestHelper.switchMap(rquestMap);
    }

    /**
     * 判断signKey是否有效
     * @param target
     * @param source
     * @return
     */
    public static void isSignKeyValidated (String target,String source){


        String signKeyFromPersisMD5 = MD5.encrypt(source);

        // 判断签名是否一致
        if(!signKeyFromPersisMD5.equals(target)){
            log.error("上传医院失败，签名不一致");
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
    }
}
