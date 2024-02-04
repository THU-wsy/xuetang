package com.thuwsy.xuetang.checkcode.dto;

import lombok.Data;

/**
 * ClassName: CheckCodeParamsDto
 * Package: com.thuwsy.xuetang.checkcode.dto
 * Description: 验证码生成参数类
 *
 * @Author THU_wsy
 * @Create 2024/1/31 12:46
 * @Version 1.0
 */
@Data
public class CheckCodeParamsDto {

    /**
     * 验证码类型:pic、sms、email等
     */
    private String checkCodeType;

    /**
     * 业务携带参数
     */
    private String param1;
    private String param2;
    private String param3;
}
