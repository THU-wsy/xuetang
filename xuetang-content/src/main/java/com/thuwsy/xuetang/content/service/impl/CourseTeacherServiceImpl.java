package com.thuwsy.xuetang.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thuwsy.xuetang.content.po.CourseTeacher;
import com.thuwsy.xuetang.content.service.CourseTeacherService;
import com.thuwsy.xuetang.content.mapper.CourseTeacherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
* @author 吴晟宇
* @description 针对表【course_teacher(课程-教师关系表)】的数据库操作Service实现
* @createDate 2024-01-12 14:44:10
*/
@Service
public class CourseTeacherServiceImpl extends ServiceImpl<CourseTeacherMapper, CourseTeacher>
    implements CourseTeacherService{
    @Autowired
    private CourseTeacherMapper courseTeacherMapper;

    @Override
    public List<CourseTeacher> getCourseTeacherList(Long courseId) {
        LambdaQueryWrapper<CourseTeacher> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseTeacher::getCourseId, courseId);
        return courseTeacherMapper.selectList(wrapper);
    }

    @Transactional
    @Override
    public CourseTeacher saveCourseTeacher(CourseTeacher courseTeacher) {
        if (courseTeacher.getId() == null) {
            // id为null则新增
            courseTeacher.setCreateDate(LocalDateTime.now());
            courseTeacherMapper.insert(courseTeacher);
            return courseTeacher;
        } else {
            // id不为null则修改
            courseTeacherMapper.updateById(courseTeacher);
            return courseTeacherMapper.selectById(courseTeacher.getId());
        }
    }

    @Transactional
    @Override
    public void deleteCourseTeacher(Long courseId, Long teacherId) {
        LambdaQueryWrapper<CourseTeacher> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseTeacher::getCourseId, courseId)
                .eq(CourseTeacher::getId, teacherId);
        courseTeacherMapper.delete(wrapper);
    }
}




