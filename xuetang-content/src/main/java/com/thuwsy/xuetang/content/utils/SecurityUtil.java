package com.thuwsy.xuetang.content.utils;

import com.thuwsy.xuetang.content.po.XcUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * ClassName: SecurityUtil
 * Package: com.thuwsy.xuetang.content.utils
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/1/31 12:17
 * @Version 1.0
 */
@Slf4j
public class SecurityUtil {
    public static XcUser getXcUser() {
        try {
            // 从SecurityContext中获取封装的用户信息
            return (XcUser) SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal();
        } catch (Exception e) {
            log.error("获取当前登录用户的身份出错：{}", e.getMessage());
            return null;
        }
    }
}
