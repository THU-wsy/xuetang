package com.thuwsy.xuetang.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * ClassName: FindPasswordDto
 * Package: com.thuwsy.xuetang.auth.dto
 * Description: 找回密码的DTO
 *
 * @Author THU_wsy
 * @Create 2024/2/4 12:46
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindPasswordDto implements Serializable {

    private String cellphone;

    private String email;

    private String checkcodekey;

    private String checkcode;

    private String password;

    private String confirmpwd;
}
