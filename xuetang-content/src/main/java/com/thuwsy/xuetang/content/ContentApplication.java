package com.thuwsy.xuetang.content;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ClassName: ContentApplication
 * Package: com.thuwsy.xuetang.content
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/1/12 15:18
 * @Version 1.0
 */
@MapperScan("com.thuwsy.xuetang.content.mapper")
@SpringBootApplication
public class ContentApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContentApplication.class, args);
    }
}
