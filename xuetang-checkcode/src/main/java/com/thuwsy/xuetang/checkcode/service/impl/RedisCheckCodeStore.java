package com.thuwsy.xuetang.checkcode.service.impl;

import com.thuwsy.xuetang.checkcode.service.CheckCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * ClassName: RedisCheckCodeStore
 * Package: com.thuwsy.xuetang.checkcode.service.impl
 * Description: 使用redis存储验证码，测试用
 *
 * @Author THU_wsy
 * @Create 2024/1/31 12:53
 * @Version 1.0
 */
@Component("RedisCheckCodeStore")
public class RedisCheckCodeStore implements CheckCodeService.CheckCodeStore {

    @Autowired
    StringRedisTemplate redisTemplate;


    @Override
    public void set(String key, String value, Integer expire) {
        redisTemplate.opsForValue().set(key,value,expire, TimeUnit.SECONDS);
    }

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(key);
    }
}
