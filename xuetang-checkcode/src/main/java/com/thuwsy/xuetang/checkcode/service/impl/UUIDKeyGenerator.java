package com.thuwsy.xuetang.checkcode.service.impl;

import com.thuwsy.xuetang.checkcode.service.CheckCodeService;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * ClassName: UUIDKeyGenerator
 * Package: com.thuwsy.xuetang.checkcode.service.impl
 * Description: uuid生成器
 *
 * @Author THU_wsy
 * @Create 2024/1/31 12:52
 * @Version 1.0
 */
@Component("UUIDKeyGenerator")
public class UUIDKeyGenerator implements CheckCodeService.KeyGenerator {
    @Override
    public String generate(String prefix) {
        String uuid = UUID.randomUUID().toString();
        return prefix + uuid.replaceAll("-", "");
    }
}
