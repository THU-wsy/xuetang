package com.thuwsy.xuetang.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * ClassName: RegisterDto
 * Package: com.thuwsy.xuetang.auth.dto
 * Description: 注册DTO
 *
 * @Author THU_wsy
 * @Create 2024/2/4 13:43
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto implements Serializable {

    private String cellphone;

    private String checkcode;

    private String checkcodekey;

    private String confirmpwd;

    private String email;

    private String nickname;

    private String password;

    private String username;
}
