package com.thuwsy.xuetang.search.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.thuwsy.xuetang.search.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * ClassName: IndexServiceImpl
 * Package: com.thuwsy.xuetang.search.service.impl
 * Description: 课程索引管理接口实现
 *
 * @Author THU_wsy
 * @Create 2024/1/24 22:53
 * @Version 1.0
 */
@Service
public class IndexServiceImpl implements IndexService  {
    @Autowired
    private ElasticsearchClient client;

    @Override
    public Boolean addCourseIndex(String indexName, String id, Object object) {
        try {
            // 新增文档
            IndexResponse response = client.index(builder -> builder
                    .index(indexName)
                    .id(id)
                    .document(object));
            // 获取结果
            String name = response.result().name();
            // 若文档不存在，则结果为CREATED；若文档存在，则结果为UPDATED
            return "CREATED".equalsIgnoreCase(name) || "UPDATED".equalsIgnoreCase(name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean updateCourseIndex(String indexName, String id, Object object) {
        // 全量修改的API与新增文档一致
        return addCourseIndex(indexName, id, object);
    }

    @Override
    public Boolean deleteCourseIndex(String indexName, String id) {
        try {
            // 删除文档
            DeleteResponse response = client.delete(builder -> builder
                    .index(indexName)
                    .id(id));
            // 获取结果
            String name = response.result().name();
            // 删除成功，则结果为DELETED
            return "DELETED".equalsIgnoreCase(name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
