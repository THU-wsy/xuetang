package com.thuwsy.xuetang.search.controller;

import com.thuwsy.xuetang.base.model.PageParams;
import com.thuwsy.xuetang.search.dto.SearchCourseParamDto;
import com.thuwsy.xuetang.search.dto.SearchPageResultDto;
import com.thuwsy.xuetang.search.po.CourseIndex;
import com.thuwsy.xuetang.search.service.CourseSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: CourseSearchController
 * Package: com.thuwsy.xuetang.search.controller
 * Description: 课程搜索接口
 *
 * @Author THU_wsy
 * @Create 2024/1/24 22:44
 * @Version 1.0
 */
@RestController
@RequestMapping("/search/course")
public class CourseSearchController {
    @Autowired
    private CourseSearchService courseSearchService;

    /**
     * 课程搜索列表
     */
    @GetMapping("/list")
    public SearchPageResultDto<CourseIndex> list(PageParams pageParams, SearchCourseParamDto searchCourseParamDto) {
        return courseSearchService.queryCoursePubIndex(pageParams,searchCourseParamDto);
    }
}
