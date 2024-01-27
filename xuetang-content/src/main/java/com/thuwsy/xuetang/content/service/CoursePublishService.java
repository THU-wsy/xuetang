package com.thuwsy.xuetang.content.service;

import com.thuwsy.xuetang.content.dto.CoursePreviewDto;
import com.thuwsy.xuetang.content.po.CoursePublish;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.PathVariable;

/**
* @author 吴晟宇
* @description 课程预览、发布service接口
* @createDate 2024-01-12 14:44:10
*/
public interface CoursePublishService {

    /**
     * 获取预览课程的信息
     * @param courseId 课程id
     * @return CoursePreviewDto
     */
    CoursePreviewDto getCoursePreviewInfo(Long courseId);

    /**
     * 提交课程进行审核
     * @param companyId 机构id
     * @param courseId 课程id
     */
    void commitAudit(Long companyId, Long courseId);

    /**
     * 发布课程
     * @param companyId 机构id
     * @param courseId 课程id
     */
    void coursePublish(Long companyId, Long courseId);

    /**
     * 将发布的课程生成静态页面，并上传至minio
     * @param courseId 课程id
     */
    void coursePublishToMinio(Long courseId);
}
