package com.thuwsy.xuetang.auth.controller;

import com.alibaba.fastjson2.JSONObject;
import com.thuwsy.xuetang.auth.dto.AuthParamsDto;
import com.thuwsy.xuetang.auth.dto.AuthResponseDto;
import com.thuwsy.xuetang.auth.mapper.XcUserMapper;
import com.thuwsy.xuetang.auth.po.XcUser;
import com.thuwsy.xuetang.auth.service.XcUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: LoginController
 * Package: com.thuwsy.xuetang.auth.controller
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/1/29 11:41
 * @Version 1.0
 */
@RestController
@RequestMapping("/auth")
public class LoginController {
    @Autowired
    private XcUserService xcUserService;
    @Autowired
    private XcUserMapper xcUserMapper;

    @PostMapping("/oauth/token")
    public AuthResponseDto login(@RequestParam("username") String username) {
        AuthParamsDto authParamsDto = JSONObject.parseObject(username, AuthParamsDto.class);
        String token = xcUserService.login(authParamsDto);
        if (token == null) {
            throw new RuntimeException("用户名密码或验证码错误");
        }
        AuthResponseDto authResponseDto = new AuthResponseDto();
        authResponseDto.setAccess_token(token);
        return authResponseDto;
    }

    @RequestMapping("/user/{id}")
    public XcUser getUser(@PathVariable("id") String id) {
        return xcUserMapper.selectById(id);
    }
}
