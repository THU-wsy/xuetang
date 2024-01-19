package com.thuwsy.xuetang.content.controller;

import com.thuwsy.xuetang.content.po.CourseTeacher;
import com.thuwsy.xuetang.content.service.CourseTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName: CourseTeacherController
 * Package: com.thuwsy.xuetang.content.controller
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/1/19 11:16
 * @Version 1.0
 */
@RestController
@RequestMapping("/content/courseTeacher")
public class CourseTeacherController {
    @Autowired
    private CourseTeacherService courseTeacherService;

    /**
     * 查询教师信息
     */
    @GetMapping("/list/{courseId}")
    public List<CourseTeacher> getCourseTeacherList(@PathVariable Long courseId) {
        return courseTeacherService.getCourseTeacherList(courseId);
    }

    /**
     * 新增/修改教师信息（id为null则新增，id不为null则修改）
     */
    @PostMapping
    public CourseTeacher saveCourseTeacher(@RequestBody CourseTeacher courseTeacher) {
        return courseTeacherService.saveCourseTeacher(courseTeacher);
    }

    /**
     * 删除教师信息
     */
    @DeleteMapping("/course/{courseId}/{teacherId}")
    public void deleteCourseTeacher(@PathVariable Long courseId, @PathVariable Long teacherId) {
        courseTeacherService.deleteCourseTeacher(courseId, teacherId);
    }
}
