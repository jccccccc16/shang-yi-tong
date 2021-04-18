package com.cjc.yygh.user.service;

import com.cjc.syt.vo.user.LoginVo;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/4/17
 * Time: 16:36
 * To change this template use File | Settings | File Templates.
 **/

public interface UserInfoService {
    /**
     * 会员登录
     * @param loginVo
     * @return
     */
    Map<String,Object> login(LoginVo loginVo);



}
