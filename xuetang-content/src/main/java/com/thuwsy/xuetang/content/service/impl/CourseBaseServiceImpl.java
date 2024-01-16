package com.thuwsy.xuetang.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thuwsy.xuetang.base.model.PageParams;
import com.thuwsy.xuetang.base.model.PageResult;
import com.thuwsy.xuetang.content.dto.QueryCourseParamsDto;
import com.thuwsy.xuetang.content.po.CourseBase;
import com.thuwsy.xuetang.content.service.CourseBaseService;
import com.thuwsy.xuetang.content.mapper.CourseBaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 吴晟宇
* @description 针对表【course_base(课程基本信息)】的数据库操作Service实现
* @createDate 2024-01-12 14:44:10
*/
@Service
public class CourseBaseServiceImpl extends ServiceImpl<CourseBaseMapper, CourseBase>
    implements CourseBaseService{

    @Autowired
    private CourseBaseMapper courseBaseMapper;

    @Override
    public PageResult<CourseBase> queryCourseBaseList(PageParams params, QueryCourseParamsDto dto) {
        // 1. 构建查询条件wrapper
        LambdaQueryWrapper<CourseBase> wrapper = new LambdaQueryWrapper<>();

        // 查询条件：课程名称（模糊查询）
        wrapper.like(StringUtils.isNotEmpty(dto.getCourseName()),
                CourseBase::getName, dto.getCourseName());
        // 查询条件：课程审核状态
        wrapper.eq(StringUtils.isNotEmpty(dto.getAuditStatus()),
                CourseBase::getAuditStatus, dto.getAuditStatus());
        // 查询条件：课程发布状态
        wrapper.eq(StringUtils.isNotEmpty(dto.getPublishStatus()),
                CourseBase::getStatus, dto.getPublishStatus());

        // 2. 分页对象
        Page<CourseBase> page = new Page<>(params.getPageNo(), params.getPageSize());

        // 3. 查询分页数据内容
        courseBaseMapper.selectPage(page, wrapper);

        // 获取当前页的数据列表
        List<CourseBase> list = page.getRecords();
        // 获取总记录数
        long total = page.getTotal();

        // 4. 构建结果集
        PageResult<CourseBase> result = new PageResult<>(list, total,
                params.getPageNo(), params.getPageSize());
        return result;
    }
}




