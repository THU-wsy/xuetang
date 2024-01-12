package com.thuwsy.xuetang.content.controller;

import com.thuwsy.xuetang.base.model.PageParams;
import com.thuwsy.xuetang.base.model.PageResult;
import com.thuwsy.xuetang.content.dto.QueryCourseParamsDto;
import com.thuwsy.xuetang.content.po.CourseBase;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: CourseBaseController
 * Package: com.thuwsy.xuetang.content.controller
 * Description: 课程信息controller
 *
 * @Author THU_wsy
 * @Create 2024/1/12 14:48
 * @Version 1.0
 */
@RestController
@RequestMapping("/course")
public class CourseBaseController {
    @PostMapping("/list")
    public PageResult<CourseBase> list(PageParams params,
            @RequestBody(required = false) QueryCourseParamsDto dto) {



        return null;
    }
}