package com.thuwsy.xuetang.content.service;

import com.thuwsy.xuetang.content.po.CourseTeacher;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 吴晟宇
* @description 针对表【course_teacher(课程-教师关系表)】的数据库操作Service
* @createDate 2024-01-12 14:44:10
*/
public interface CourseTeacherService extends IService<CourseTeacher> {

    /**
     * 查询教师信息
     */
    List<CourseTeacher> getCourseTeacherList(Long courseId);

    /**
     * 新增/修改教师信息（id为null则新增，id不为null则修改）
     */
    CourseTeacher saveCourseTeacher(CourseTeacher courseTeacher);

    /**
     * 删除教师信息
     */
    void deleteCourseTeacher(Long courseId, Long teacherId);
}
