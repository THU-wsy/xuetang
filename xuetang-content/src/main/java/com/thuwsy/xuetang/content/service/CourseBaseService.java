package com.thuwsy.xuetang.content.service;

import com.thuwsy.xuetang.base.model.PageParams;
import com.thuwsy.xuetang.base.model.PageResult;
import com.thuwsy.xuetang.content.dto.AddCourseDto;
import com.thuwsy.xuetang.content.dto.CourseBaseInfoDto;
import com.thuwsy.xuetang.content.dto.EditCourseDto;
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
    PageResult<CourseBase> queryCourseBaseList(Long companyId, PageParams params, QueryCourseParamsDto dto);

    /**
     * 添加课程基本信息
     * @param companyId 教学机构id
     * @param dto 课程基本信息
     * @return CourseBaseInfoDto
     */
    CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto dto);

    /**
     * 根据课程id查询课程信息
     * @param id 课程id
     * @return CourseBaseInfoDto
     */
    CourseBaseInfoDto getCourseBaseById(Long id);

    /**
     * 修改课程信息
     * @param companyId 机构id
     * @param dto 课程信息DTO
     * @return CourseBaseInfoDto
     */
    CourseBaseInfoDto modifyCourseBase(Long companyId, EditCourseDto dto);

    /**
     * 删除课程
     * @param companyId 机构id
     * @param courseId 课程id
     */
    void deleteCourse(Long companyId, Long courseId);
}
