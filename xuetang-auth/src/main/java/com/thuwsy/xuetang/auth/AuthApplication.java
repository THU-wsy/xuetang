package com.thuwsy.xuetang.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * ClassName: AuthApplication
 * Package: com.thuwsy.xuetang.auth
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/1/29 10:53
 * @Version 1.0
 */
@EnableFeignClients
@MapperScan("com.thuwsy.xuetang.auth.mapper")
@SpringBootApplication
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
