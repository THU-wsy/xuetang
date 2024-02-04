package com.thuwsy.xuetang.checkcode.service;

/**
 * ClassName: SendCodeService
 * Package: com.thuwsy.xuetang.checkcode.service
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/2/4 12:49
 * @Version 1.0
 */
public interface SendCodeService {
    /**
     * 向目标邮箱发送验证码
     * @param email 目标邮箱
     * @param code 验证码
     */
    void sendEMail(String email, String code);

}
