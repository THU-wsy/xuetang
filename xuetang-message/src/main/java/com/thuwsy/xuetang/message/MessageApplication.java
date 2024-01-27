package com.thuwsy.xuetang.message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * ClassName: MessageApplication
 * Package: com.thuwsy.xuetang.message
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/1/24 14:22
 * @Version 1.0
 */
@EnableFeignClients
@SpringBootApplication
public class MessageApplication {
    public static void main(String[] args) {
        SpringApplication.run(MessageApplication.class, args);
    }
}
