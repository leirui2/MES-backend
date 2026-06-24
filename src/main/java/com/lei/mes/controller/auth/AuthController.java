package com.lei.mes.controller.auth;

import com.lei.mes.annotation.OperationLog;
import com.lei.mes.common.Result;
import com.lei.mes.request.user.UserLoginRequest;
import com.lei.mes.service.user.SysUserService;
import com.lei.mes.vo.user.LoginResponseVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 认证管理 Controller
 * @author lei
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 用户登录
     * @param request 用户登录请求体
     * @return 登录响应
     */
    @PostMapping("/login")
    public Result<LoginResponseVO> login(@Valid @RequestBody UserLoginRequest request){
        LoginResponseVO loginResponseVO = sysUserService.login(request);
        return Result.success(loginResponseVO);
    }


}
