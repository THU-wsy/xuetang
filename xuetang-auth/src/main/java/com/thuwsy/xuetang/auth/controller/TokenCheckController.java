package com.thuwsy.xuetang.auth.controller;

import com.alibaba.fastjson2.JSONObject;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.thuwsy.xuetang.auth.dto.CheckTokenResponseDto;
import com.thuwsy.xuetang.auth.po.XcUser;
import com.thuwsy.xuetang.auth.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: TokenCheckController
 * Package: com.thuwsy.xuetang.auth.controller
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/1/31 11:47
 * @Version 1.0
 */
@RestController
@RequestMapping("/auth")
public class TokenCheckController {
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/oauth/check_token")
    public CheckTokenResponseDto checkToken(String token) {
        String headerToken = "Bearer " + token;
        DecodedJWT decodedJWT = jwtUtil.resolveJwt(headerToken);
        if (decodedJWT == null) return null;

        String userJson = jwtUtil.toUser(decodedJWT);
        XcUser xcUser = JSONObject.parseObject(userJson, XcUser.class);
        long exp = decodedJWT.getExpiresAt().getTime();
        String jti = decodedJWT.getId();

        CheckTokenResponseDto dto = new CheckTokenResponseDto();
        dto.setJti(jti);
        dto.setExp(exp);
        dto.setUser_name(userJson);
        dto.setAuthorities(xcUser.getPermissions());
        return dto;
    }
}
