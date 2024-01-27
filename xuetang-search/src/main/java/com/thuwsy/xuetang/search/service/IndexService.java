package com.thuwsy.xuetang.search.service;

/**
 * ClassName: IndexService
 * Package: com.thuwsy.xuetang.search.service
 * Description: 课程文档service
 *
 * @Author THU_wsy
 * @Create 2024/1/24 22:35
 * @Version 1.0
 */
public interface IndexService {

    /**
     * 添加文档
     * @param indexName 索引库名称
     * @param id 主键
     * @param object 文档对象
     * @return true成功, false失败
     */
    Boolean addCourseIndex(String indexName, String id, Object object);


    /**
     * 更新文档
     * @param indexName 索引库名称
     * @param id 主键
     * @param object 文档对象
     * @return true成功, false失败
     */
    Boolean updateCourseIndex(String indexName, String id, Object object);

    /**
     * 删除文档
     * @param indexName 索引库名称
     * @param id 主键
     * @return true成功, false失败
     */
    Boolean deleteCourseIndex(String indexName, String id);
}
