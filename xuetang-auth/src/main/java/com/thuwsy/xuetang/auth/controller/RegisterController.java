package com.thuwsy.xuetang.auth.controller;

import com.thuwsy.xuetang.auth.dto.FindPasswordDto;
import com.thuwsy.xuetang.auth.dto.RegisterDto;
import com.thuwsy.xuetang.auth.service.VerifyService;
import com.thuwsy.xuetang.auth.service.XcUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: RegisterController
 * Package: com.thuwsy.xuetang.auth.controller
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/2/4 12:48
 * @Version 1.0
 */
@RestController
@RequestMapping("/auth")
public class RegisterController {
    @Autowired
    private VerifyService verifyService;

    /**
     * 找回密码
     */
    @PostMapping("/findpassword")
    public void findPassword(@RequestBody FindPasswordDto dto) {
        verifyService.findPassword(dto);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public void register(@RequestBody RegisterDto registerDto) {
        verifyService.register(registerDto);
    }
}
