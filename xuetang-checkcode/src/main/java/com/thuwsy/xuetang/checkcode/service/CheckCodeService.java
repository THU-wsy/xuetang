package com.thuwsy.xuetang.checkcode.service;

import com.thuwsy.xuetang.checkcode.dto.CheckCodeParamsDto;
import com.thuwsy.xuetang.checkcode.dto.CheckCodeResultDto;

/**
 * ClassName: CheckCodeService
 * Package: com.thuwsy.xuetang.checkcode.service
 * Description: 验证码接口
 *
 * @Author THU_wsy
 * @Create 2024/1/31 12:47
 * @Version 1.0
 */
public interface CheckCodeService {


    /**
     * 生成验证码
     * @param checkCodeParamsDto 生成验证码参数
     * @return 验证码结果
     */
    CheckCodeResultDto generate(CheckCodeParamsDto checkCodeParamsDto);

    /**
     * 校验验证码
     * @param key
     * @param code
     * @return boolean
     */
    boolean verify(String key, String code);


    /**
     * 验证码生成器
     */
    interface CheckCodeGenerator{
        /**
         * 验证码生成
         */
        String generate(int length);
    }

    /**
     * key生成器
     */
    interface KeyGenerator{

        /**
         * key生成
         */
        String generate(String prefix);
    }


    /**
     * 验证码存储
     */
    interface CheckCodeStore{

        /**
         * 向缓存设置key
         * @param key key
         * @param value value
         * @param expire 过期时间,单位秒
         */
        void set(String key, String value, Integer expire);

        String get(String key);

        void remove(String key);
    }
}
