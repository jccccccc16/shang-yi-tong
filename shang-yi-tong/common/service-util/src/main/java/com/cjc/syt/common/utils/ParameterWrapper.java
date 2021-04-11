package com.cjc.syt.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.cjc.syt.common.exception.YyghException;
import com.cjc.syt.common.helper.HttpRequestHelper;
import com.cjc.syt.common.result.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;



import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/4/1
 * Time: 10:13
 * To change this template use File | Settings | File Templates.
 **/
@Slf4j
public class ParameterWrapper implements InvocationHandler {

    private Map<String, Object> parameterMap;

    private ParameterWrapper(Map<String, Object> parameterMap){
        this.parameterMap = parameterMap;
    }

    /**
     * 创建一个paramterWrapper
     * @param request
     * @return
     */
    public static ParameterWrapper build(HttpServletRequest request){
        Map<String, String[]> rquestMap = request.getParameterMap();
        Map<String, Object> parameterMap = HttpRequestHelper.switchMap(rquestMap);
        return new ParameterWrapper(parameterMap);
    }

    public Map<String, Object> getParameterMap(){
        return this.parameterMap;
    }

    public String getParameter(String parameterName){

        return (String) parameterMap.get(parameterName);
    }

    /**
     * 获取医院编号
     * @return
     */
    public String getHospcode(){
        return this.getParameter("hoscode");
    }

    /**
     * 获取签名
     * @return
     */
    public String getSignKey(){
        return this.getParameter("sign");
    }

    /**
     * 判断signKey是否有效
     * @param targetMD5
     * @param source
     * @return
     */
    public void isSignKeyValidated (String targetMD5,String source){

        // 对source加密
        String signKeyFromPersisMD5 = MD5.encrypt(source);

        // 判断签名是否一致
        if(!signKeyFromPersisMD5.equals(targetMD5)){
            log.error("上传医院失败，签名不一致");
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
    }

    public <T> T toObject(Class<T> clazz){
        String jsonString = JSONObject.toJSONString(this.parameterMap);
        return JSONObject.parseObject(jsonString, clazz);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ParameterWrapper parameterWrapper = (ParameterWrapper)proxy;
        if(parameterWrapper.getParameterMap() == null || parameterWrapper.getParameterMap().size() == 0){
            throw new RuntimeException(" parameterMap 为空，请调用build方法进行初始化");
        }
        return null;
    }
}
