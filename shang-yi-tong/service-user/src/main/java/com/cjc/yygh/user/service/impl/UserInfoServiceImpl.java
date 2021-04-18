package com.cjc.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjc.syt.common.exception.YyghException;
import com.cjc.syt.common.helper.JwtHelper;
import com.cjc.syt.common.result.ResultCodeEnum;
import com.cjc.syt.model.user.UserInfo;
import com.cjc.syt.vo.user.LoginVo;
import com.cjc.yygh.user.mapper.UserInfoMapper;
import com.cjc.yygh.user.service.UserInfoService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/4/17
 * Time: 16:37
 * To change this template use File | Settings | File Templates.
 **/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    /**
     * 手机登录接口
     * @param loginVo
     * @return
     */
    @Override
    public Map<String, Object> login(LoginVo loginVo) {

        String code = loginVo.getCode();
        String phone = loginVo.getPhone();

        // 手机号与验证码是否为空
        if(StringUtils.isEmpty(code)|| StringUtils.isEmpty(phone)){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        //TODO 判断是否验证码是否有效
        if(!"369".equals(loginVo.getCode())){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        //手机号是否存在
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone",phone);
        UserInfo userInfo = baseMapper.selectOne(queryWrapper);
        // 不存在，则保存
        if(null == userInfo){
            userInfo = new UserInfo();
            userInfo.setName("");
            userInfo.setStatus(1);
            baseMapper.insert(userInfo);
        }
        // 存在，则直接登录
        if(userInfo.getStatus() == 0){
            throw new YyghException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }
        // 返回用户名与token（用于验证登录）
        String name = userInfo.getName();
        if(StringUtils.isEmpty(name)){
            name = userInfo.getNickName();
        }
        if(StringUtils.isEmpty(name)){
            name = userInfo.getPhone();
        }
        Map<String,Object> map = new HashMap<>();
        map.put("name",userInfo.getName());
        //TODO 生成token
        String token = JwtHelper.createToken(userInfo.getId(), userInfo.getName());
        map.put("token",token);
        return map;
    }
}
