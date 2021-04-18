package com.cjc.yygh.user.api;

import com.cjc.syt.common.result.Result;
import com.cjc.syt.vo.user.LoginVo;
import com.cjc.yygh.user.service.UserInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/4/17
 * Time: 16:35
 * To change this template use File | Settings | File Templates.
 **/
@RestController
@RequestMapping("/api/user")
public class UserInfoApiController {

    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo){
        Map<String,Object> resultMap =  userInfoService.login(loginVo);
        return Result.ok(resultMap);
    }

}
