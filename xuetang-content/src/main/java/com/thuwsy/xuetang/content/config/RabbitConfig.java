package com.thuwsy.xuetang.content.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: RabbitConfig
 * Package: com.thuwsy.xuetang.content.config
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/1/24 13:54
 * @Version 1.0
 */
@Configuration
public class RabbitConfig {
    @Bean
    public FanoutExchange fanoutExchange() {
        return ExchangeBuilder
                .fanoutExchange("exchange.xuetang.publish")
                .build();
    }

    @Bean
    public Queue minioQueue() {
        return QueueBuilder.durable("queue.xuetang.minio").build();
    }

    @Bean
    public Queue esQueue() {
        return QueueBuilder.durable("queue.xuetang.es").build();
    }

    @Bean
    public Queue redisQueue() {
        return QueueBuilder.durable("queue.xuetang.redis").build();
    }

    @Bean
    public Binding minioBinding(FanoutExchange fanoutExchange, Queue minioQueue) {
        return BindingBuilder.bind(minioQueue).to(fanoutExchange);
    }

    @Bean
    public Binding esBinding(FanoutExchange fanoutExchange, Queue esQueue) {
        return BindingBuilder.bind(esQueue).to(fanoutExchange);
    }

    @Bean
    public Binding redisBinding(FanoutExchange fanoutExchange, Queue redisQueue) {
        return BindingBuilder.bind(redisQueue).to(fanoutExchange);
    }
}
