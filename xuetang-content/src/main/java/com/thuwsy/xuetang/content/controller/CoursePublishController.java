package com.thuwsy.xuetang.content.controller;

import com.thuwsy.xuetang.content.dto.CoursePreviewDto;
import com.thuwsy.xuetang.content.service.CoursePublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * ClassName: CoursePublishController
 * Package: com.thuwsy.xuetang.content.controller
 * Description: 课程预览、发布Controller
 *
 * @Author THU_wsy
 * @Create 2024/1/22 20:39
 * @Version 1.0
 */
@Controller
@RequestMapping("/content")
public class CoursePublishController {

    @Autowired
    private CoursePublishService coursePublishService;

    /**
     * 课程预览
     */
    @GetMapping("/coursepreview/{courseId}")
    public ModelAndView preview(@PathVariable("courseId") Long courseId) {
        // 获取课程预览信息
        CoursePreviewDto coursePreviewInfo = coursePublishService.getCoursePreviewInfo(courseId);

        ModelAndView mav = new ModelAndView();
        mav.addObject("model", coursePreviewInfo);
        mav.setViewName("course_template");
        return mav;
    }

    /**
     * 提交课程进行审核
     */
    @PostMapping("/courseaudit/commit/{courseId}")
    public void commitAudit(@PathVariable Long courseId) {
        Long companyId = 1232141425L;
        coursePublishService.commitAudit(companyId, courseId);
    }
}
