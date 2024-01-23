package com.thuwsy.xuetang.content.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thuwsy.xuetang.base.exception.XueTangException;
import com.thuwsy.xuetang.content.dto.CourseBaseInfoDto;
import com.thuwsy.xuetang.content.dto.CoursePreviewDto;
import com.thuwsy.xuetang.content.dto.TeachplanDto;
import com.thuwsy.xuetang.content.mapper.CourseBaseMapper;
import com.thuwsy.xuetang.content.mapper.CourseMarketMapper;
import com.thuwsy.xuetang.content.mapper.CoursePublishPreMapper;
import com.thuwsy.xuetang.content.po.CourseBase;
import com.thuwsy.xuetang.content.po.CourseMarket;
import com.thuwsy.xuetang.content.po.CoursePublish;
import com.thuwsy.xuetang.content.po.CoursePublishPre;
import com.thuwsy.xuetang.content.service.CourseBaseService;
import com.thuwsy.xuetang.content.service.CoursePublishService;
import com.thuwsy.xuetang.content.mapper.CoursePublishMapper;
import com.thuwsy.xuetang.content.service.TeachplanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
* @author 吴晟宇
* @description 针对表【course_publish(课程发布)】的数据库操作Service实现
* @createDate 2024-01-12 14:44:10
*/
@Service
public class CoursePublishServiceImpl implements CoursePublishService {

    @Autowired
    private CourseBaseService courseBaseService;

    @Autowired
    private TeachplanService teachplanService;

    @Autowired
    private CourseBaseMapper courseBaseMapper;

    @Autowired
    private CourseMarketMapper courseMarketMapper;

    @Autowired
    private CoursePublishPreMapper coursePublishPreMapper;

    /**
     * 获取预览课程的信息
     * @param courseId 课程id
     * @return CoursePreviewDto
     */
    @Override
    public CoursePreviewDto getCoursePreviewInfo(Long courseId) {
        // 1. 获取课程基本信息、课程营销信息
        CourseBaseInfoDto courseBase = courseBaseService.getCourseBaseById(courseId);

        // 2. 获取课程计划信息
        List<TeachplanDto> teachplans = teachplanService.findTeachplanTree(courseId);

        // 3. ========获取师资信息

        CoursePreviewDto coursePreviewDto = new CoursePreviewDto();
        coursePreviewDto.setCourseBase(courseBase);
        coursePreviewDto.setTeachplans(teachplans);
        return coursePreviewDto;
    }

    /**
     * 提交课程进行审核
     * @param companyId 机构id
     * @param courseId 课程id
     */
    @Transactional
    @Override
    public void commitAudit(Long companyId, Long courseId) {
        // 1. 查询课程的相关信息
        // 查询课程基本信息、课程营销信息
        CourseBaseInfoDto courseBaseInfo = courseBaseService.getCourseBaseById(courseId);
        // 查询课程营销信息（用于设置json格式的market字段）
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        // 查询课程计划
        List<TeachplanDto> teachplanTree = teachplanService.findTeachplanTree(courseId);

        // 2. 约束校验
        // 当前课程审核状态为已提交时，不可再次提交
        if ("202003".equals(courseBaseInfo.getAuditStatus())) {
            XueTangException.cast("该课程正在审核中，审核完成后才可以再次提交");
        }
        // 只允许提交本机构的课程
        if (!companyId.equals(courseBaseInfo.getCompanyId())) {
            XueTangException.cast("不允许提交其他机构的课程");
        }
        // 课程没有上传图片，不允许提交
        if (StringUtils.isEmpty(courseBaseInfo.getPic())) {
            XueTangException.cast("提交失败，请上传课程图片");
        }
        // 没有添加课程计划，不允许提交
        if (teachplanTree == null || teachplanTree.isEmpty()) {
            XueTangException.cast("没有添加课程计划，不允许提交");
        }

        // 3. 封装课程预发布记录

        CoursePublishPre coursePublishPre = new CoursePublishPre();
        BeanUtils.copyProperties(courseBaseInfo, coursePublishPre);
        coursePublishPre.setMarket(JSON.toJSONString(courseMarket));
        coursePublishPre.setTeachplan(JSON.toJSONString(teachplanTree));
        coursePublishPre.setCompanyId(companyId);
        coursePublishPre.setCreateDate(LocalDateTime.now());
        coursePublishPre.setStatus("202003"); // 设置审核状态为已提交

        // 4. 判断是否已存在该课程的预发布记录。若存在则更新，否则就新增。
        CoursePublishPre tmp = coursePublishPreMapper.selectById(courseId);
        if (tmp == null) {
            coursePublishPreMapper.insert(coursePublishPre);
        } else {
            coursePublishPreMapper.updateById(coursePublishPre);
        }

        // 5. 将课程基本信息表中该课程的审核状态修改为已提交
        CourseBase courseBase = new CourseBase();
        courseBase.setAuditStatus("202003");
        courseBase.setId(courseId);
        courseBaseMapper.updateById(courseBase);
    }
}




