package com.thuwsy.xuetang.content.service;

import com.thuwsy.xuetang.base.model.PageParams;
import com.thuwsy.xuetang.base.model.PageResult;
import com.thuwsy.xuetang.content.dto.QueryCourseParamsDto;
import com.thuwsy.xuetang.content.po.CourseBase;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 吴晟宇
* @description 针对表【course_base(课程基本信息)】的数据库操作Service
* @createDate 2024-01-12 14:44:10
*/
public interface CourseBaseService extends IService<CourseBase> {
    /**
     * 查询课程
     * @param params 分页参数
     * @param dto 查询条件
     * @return PageResult
     */
    PageResult<CourseBase> queryCourseBaseList(PageParams params, QueryCourseParamsDto dto);
}
