package com.thuwsy.xuetang.auth.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.thuwsy.xuetang.auth.dto.AuthParamsDto;
import com.thuwsy.xuetang.auth.dto.RegisterDto;
import com.thuwsy.xuetang.auth.feign.CheckcodeFeignService;
import com.thuwsy.xuetang.auth.po.XcUser;
import com.thuwsy.xuetang.auth.service.XcUserService;
import com.thuwsy.xuetang.auth.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * ClassName: XcUserServiceImpl
 * Package: com.thuwsy.xuetang.auth.service.impl
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/1/30 22:20
 * @Version 1.0
 */
@Service
public class XcUserServiceImpl implements XcUserService {
    // SpringSecurity中使用AuthenticationManager验证账号密码
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CheckcodeFeignService checkcodeFeignService;

    @Override
    public String login(AuthParamsDto authParamsDto) {
        // 校验验证码
        String checkcode = authParamsDto.getCheckcode();
        String checkcodekey = authParamsDto.getCheckcodekey();
        if (StringUtils.isBlank(checkcodekey) || StringUtils.isBlank(checkcode))
            return null;

        Boolean verify = checkcodeFeignService.verify(checkcodekey, checkcode);
        if (!verify) return null;

        // 1. 传入要验证的用户名和密码
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        authParamsDto.getUsername(),
                        authParamsDto.getPassword()
                );

        // 2. 调用AuthenticationManager的authenticate()方法验证用户名和密码
        // 其底层会调用UserDetailsService的loadUserByUsername()方法获取用户
        XcUser xcUser = null;
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            // 3. 获取用户信息
            xcUser = (XcUser) authentication.getPrincipal();
            if (xcUser == null) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
        // 4. 根据用户的公开信息创建JWT令牌并返回（注意一定要将隐私信息置空）
        xcUser.setPassword(null);
        String userJson = JSON.toJSONString(xcUser);
        return jwtUtil.createJwt(userJson);
    }
}



