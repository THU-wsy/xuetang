package com.thuwsy.xuetang.search.service;

import com.thuwsy.xuetang.base.model.PageParams;
import com.thuwsy.xuetang.search.dto.SearchCourseParamDto;
import com.thuwsy.xuetang.search.dto.SearchPageResultDto;
import com.thuwsy.xuetang.search.po.CourseIndex;

/**
 * ClassName: CourseSearchService
 * Package: com.thuwsy.xuetang.search.service
 * Description: 课程搜索service
 *
 * @Author THU_wsy
 * @Create 2024/1/24 22:35
 * @Version 1.0
 */
public interface CourseSearchService {
    /**
     * 搜索课程列表
     * @param pageParams 分页参数
     * @param searchCourseParamDto 搜索条件
     * @return SearchPageResultDto<CourseIndex>
     */
    SearchPageResultDto<CourseIndex> queryCoursePubIndex(PageParams pageParams, SearchCourseParamDto searchCourseParamDto);
}
