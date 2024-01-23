package com.thuwsy.xuetang.content.controller;

import com.thuwsy.xuetang.content.dto.CoursePreviewDto;
import com.thuwsy.xuetang.content.service.CourseBaseService;
import com.thuwsy.xuetang.content.service.CoursePublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: CourseOpenController
 * Package: com.thuwsy.xuetang.content.controller
 * Description: 课程公开查询接口
 *
 * @Author THU_wsy
 * @Create 2024/1/22 21:34
 * @Version 1.0
 */
@RestController
@RequestMapping("/content/open")
public class CourseOpenController {
    @Autowired
    private CourseBaseService courseBaseService;

    @Autowired
    private CoursePublishService coursePublishService;

    @GetMapping("/course/whole/{courseId}")
    public CoursePreviewDto getPreviewInfo(@PathVariable Long courseId) {
        // 获取课程预览信息
        return coursePublishService.getCoursePreviewInfo(courseId);
    }
}
