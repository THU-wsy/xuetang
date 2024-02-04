package com.thuwsy.xuetang.checkcode.dto;

import lombok.Data;

/**
 * ClassName: CheckCodeResultDto
 * Package: com.thuwsy.xuetang.checkcode.dto
 * Description: 验证码生成结果类
 *
 * @Author THU_wsy
 * @Create 2024/1/31 12:46
 * @Version 1.0
 */
@Data
public class CheckCodeResultDto {

    /**
     * key用于验证
     */
    private String key;

    /**
     * 混淆后的内容
     * 举例：
     * 1.图片验证码为:图片base64编码
     * 2.短信验证码为:null
     * 3.邮件验证码为: null
     * 4.邮件链接点击验证为：null
     * ...
     */
    private String aliasing;
}
