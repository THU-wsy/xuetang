package com.thuwsy.xuetang.search.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: ElasticsearchConfig
 * Package: com.thuwsy.xuetang.search.config
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/1/24 22:18
 * @Version 1.0
 */
@Configuration
public class ElasticsearchConfig {
    @Value("${elasticsearch.host}")
    private String host;
    @Value("${elasticsearch.port}")
    private Integer port;

    @Bean // 初始化es客户端
    public ElasticsearchClient elasticsearchClient() {
        RestClient restClient = RestClient.builder(new HttpHost(host, port)).build();
        RestClientTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }
}
