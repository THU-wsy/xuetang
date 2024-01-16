package com.thuwsy.xuetang.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ClassName: SystemApplication
 * Package: com.thuwsy.xuetang.system
 * Description: 系统管理启动类
 *
 * @Author THU_wsy
 * @Create 2024/1/16 20:52
 * @Version 1.0
 */
@MapperScan("com.thuwsy.xuetang.system.mapper")
@SpringBootApplication
public class SystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
    }
}
