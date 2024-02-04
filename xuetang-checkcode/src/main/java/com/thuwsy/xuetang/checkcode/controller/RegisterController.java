package com.thuwsy.xuetang.checkcode.controller;

import com.thuwsy.xuetang.checkcode.service.SendCodeService;
import com.thuwsy.xuetang.checkcode.utils.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: RegisterController
 * Package: com.thuwsy.xuetang.checkcode.controller
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/2/4 12:44
 * @Version 1.0
 */
@RestController
@RequestMapping("/checkcode")
public class RegisterController {
    @Autowired
    private SendCodeService sendCodeService;

    /**
     * 发送邮箱验证码
     * @param email 邮箱
     */
    @PostMapping("/phone")
    public void sendEMail(@RequestParam("param1") String email) {
        String code = MailUtil.achieveCode();
        sendCodeService.sendEMail(email, code);
    }
}
