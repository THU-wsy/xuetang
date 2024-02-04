package com.thuwsy.xuetang.auth.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * ClassName: AuthResponseDto
 * Package: com.thuwsy.xuetang.auth.dto
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/1/30 22:11
 * @Version 1.0
 */
@Data
public class AuthResponseDto implements Serializable {
    private String access_token;
    private String token_type = "bearer";
    private String refresh_token = "123";
    private Integer expires_in = 72 * 60 * 60;
    private String scope = "all";
    private String jti = "123";
}
