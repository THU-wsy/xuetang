package com.thuwsy.xuetang.media;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ClassName: MediaApplication
 * Package: com.thuwsy.xuetang.media
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/1/19 18:30
 * @Version 1.0
 */
@MapperScan("com.thuwsy.xuetang.media.mapper")
@SpringBootApplication
public class MediaApplication {
    public static void main(String[] args) {
        SpringApplication.run(MediaApplication.class, args);
    }
}
