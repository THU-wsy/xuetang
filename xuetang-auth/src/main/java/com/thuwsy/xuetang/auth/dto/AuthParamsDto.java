package com.thuwsy.xuetang.auth.dto;

import lombok.Data;

/**
 * ClassName: AuthParamsDto
 * Package: com.thuwsy.xuetang.auth.dto
 * Description: 认证用户请求参数
 *
 * @Author THU_wsy
 * @Create 2024/1/29 10:57
 * @Version 1.0
 */
@Data
public class AuthParamsDto {
    private String username; // 用户名
    private String password; // 密码
    private String checkcode; //验证码
    private String checkcodekey;//验证码key
}
