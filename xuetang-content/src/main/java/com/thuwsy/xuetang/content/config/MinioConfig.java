package com.thuwsy.xuetang.content.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: MinioConfig
 * Package: com.thuwsy.xuetang.content.config
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/1/24 19:52
 * @Version 1.0
 */
@Configuration
public class MinioConfig {
    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Bean // minio客户端
    public MinioClient minioClient() {
        return MinioClient.builder().endpoint(endpoint)
                .credentials(accessKey, secretKey).build();
    }
}
