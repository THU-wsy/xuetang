package com.thuwsy.xuetang.checkcode.service.impl;

import com.thuwsy.xuetang.base.exception.XueTangException;
import com.thuwsy.xuetang.checkcode.service.SendCodeService;
import com.thuwsy.xuetang.checkcode.utils.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * ClassName: SendCodeServiceImpl
 * Package: com.thuwsy.xuetang.checkcode.service.impl
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/2/4 12:51
 * @Version 1.0
 */
@Service
@Slf4j
public class SendCodeServiceImpl implements SendCodeService {
    public final Long CODE_TTL = 120L;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private MailUtil mailUtil;

    @Override
    public void sendEMail(String email, String code) {
        // 1. 向用户发送验证码
        try {
            mailUtil.sendMail(email, code);
        } catch (Exception e) {
            log.debug("邮件发送失败：{}", e.getMessage());
            XueTangException.cast("发送验证码失败，请稍后再试");
        }
        // 2. 将验证码缓存到redis，TTL设置为2分钟
        redisTemplate.opsForValue().set(email, code, CODE_TTL, TimeUnit.SECONDS);
    }
}
