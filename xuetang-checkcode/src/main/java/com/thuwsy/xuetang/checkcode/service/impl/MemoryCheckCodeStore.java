package com.thuwsy.xuetang.checkcode.service.impl;

import com.thuwsy.xuetang.checkcode.service.CheckCodeService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: MemoryCheckCodeStore
 * Package: com.thuwsy.xuetang.checkcode.service.impl
 * Description: 使用本地内存存储验证码，测试用
 *
 * @Author THU_wsy
 * @Create 2024/1/31 13:03
 * @Version 1.0
 */
@Component("MemoryCheckCodeStore")
public class MemoryCheckCodeStore implements CheckCodeService.CheckCodeStore {

    Map<String,String> map = new HashMap<>();

    @Override
    public void set(String key, String value, Integer expire) {
        map.put(key,value);
    }

    @Override
    public String get(String key) {
        return map.get(key);
    }

    @Override
    public void remove(String key) {
        map.remove(key);
    }
}
