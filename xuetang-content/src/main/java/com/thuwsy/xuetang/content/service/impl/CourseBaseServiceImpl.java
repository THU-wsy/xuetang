package com.thuwsy.xuetang.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thuwsy.xuetang.base.exception.XueTangException;
import com.thuwsy.xuetang.base.model.PageParams;
import com.thuwsy.xuetang.base.model.PageResult;
import com.thuwsy.xuetang.content.dto.AddCourseDto;
import com.thuwsy.xuetang.content.dto.CourseBaseInfoDto;
import com.thuwsy.xuetang.content.dto.EditCourseDto;
import com.thuwsy.xuetang.content.dto.QueryCourseParamsDto;
import com.thuwsy.xuetang.content.mapper.*;
import com.thuwsy.xuetang.content.po.CourseBase;
import com.thuwsy.xuetang.content.po.CourseMarket;
import com.thuwsy.xuetang.content.po.CourseTeacher;
import com.thuwsy.xuetang.content.po.Teachplan;
import com.thuwsy.xuetang.content.service.CourseBaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Autowired
    private CourseMarketMapper courseMarketMapper;

    @Autowired
    private CourseCategoryMapper courseCategoryMapper;

    @Autowired
    private TeachplanMapper teachplanMapper;

    @Autowired
    private CourseTeacherMapper courseTeacherMapper;

    @Override
    public PageResult<CourseBase> queryCourseBaseList(Long companyId, PageParams params, QueryCourseParamsDto dto) {
        // 1. 构建查询条件wrapper
        LambdaQueryWrapper<CourseBase> wrapper = new LambdaQueryWrapper<>();

        // 查询条件：机构id相同
        wrapper.eq(CourseBase::getCompanyId, companyId);
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

    @Transactional
    @Override
    public CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto dto) {
        // 1. 新增课程基本信息
        CourseBase courseBase = new CourseBase();
        // 将请求增加的课程基本信息进行赋值（属性名称和类型相同的才进行赋值）
        BeanUtils.copyProperties(dto, courseBase);
        courseBase.setAuditStatus("202002"); // 审核状态为未提交
        courseBase.setStatus("203001"); // 发布状态为未发布
        courseBase.setCompanyId(companyId); // 机构id
        courseBase.setCreateDate(LocalDateTime.now()); // 创建时间

        // 2. 向课程基本信息表中插入数据
        int res1 = courseBaseMapper.insert(courseBase);
        if (res1 <= 0) {
            XueTangException.cast("新增课程基本信息失败");
        }

        // 3. 新增课程营销信息
        CourseMarket courseMarket = new CourseMarket();
        // 将请求增加的课程营销信息进行赋值（属性名称和类型相同的才进行赋值）
        BeanUtils.copyProperties(dto, courseMarket);
        // 设置课程基本信息表和课程营销信息表相关联的字段（id）
        courseMarket.setId(courseBase.getId());

        // 4. 向课程营销信息表中插入数据
        int res2 = saveCourseMarket(courseMarket);
        if (res2 <= 0) {
            XueTangException.cast("保存课程营销信息失败");
        }

        // 5. 组装返回结果
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        // 赋值课程基本信息
        BeanUtils.copyProperties(courseBase, courseBaseInfoDto);
        // 赋值课程营销信息
        BeanUtils.copyProperties(courseMarket, courseBaseInfoDto);
        // 查询分类名称，并赋值
        courseBaseInfoDto.setMtName(
            courseCategoryMapper.selectById(courseBase.getMt()).getName()
        );
        courseBaseInfoDto.setStName(
            courseCategoryMapper.selectById(courseBase.getSt()).getName()
        );

        return courseBaseInfoDto;
    }

    /**
     * 向课程营销信息表中插入或修改记录
     */
    private int saveCourseMarket(CourseMarket courseMarket) {
        // 1. 参数合法性校验
        String charge = courseMarket.getCharge(); // 收费规则
        if (StringUtils.isBlank(charge)) {
            XueTangException.cast("收费规则没有选择");
        }

        // 若收费规则为收费
        if (charge.equals("201001")) {
            if (courseMarket.getPrice() == null || courseMarket.getPrice() <= 0)
                XueTangException.cast("收费课程价格必须大于0");
        }

        // 2. 查询课程营销表中是否已有该记录
        CourseMarket obj = courseMarketMapper.selectById(courseMarket.getId());

        if (obj == null) {
            // 3. 没有记录，则新增
            return courseMarketMapper.insert(courseMarket);
        } else {
            // 4. 有该记录，则修改
            return courseMarketMapper.updateById(courseMarket);
        }
    }

    @Override
    public CourseBaseInfoDto getCourseBaseById(Long id) {
        // 1. 查询课程基本信息
        CourseBase courseBase = courseBaseMapper.selectById(id);
        if (courseBase == null)
            return null;

        // 2. 查询课程营销信息
        CourseMarket courseMarket = courseMarketMapper.selectById(id);

        // 3. 组装返回结果
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        // 赋值课程基本信息
        BeanUtils.copyProperties(courseBase, courseBaseInfoDto);
        // 赋值课程营销信息
        if (courseMarket != null) {
            BeanUtils.copyProperties(courseMarket, courseBaseInfoDto);
        }
        // 查询分类名称，并赋值
        courseBaseInfoDto.setMtName(
            courseCategoryMapper.selectById(courseBase.getMt()).getName()
        );
        courseBaseInfoDto.setStName(
            courseCategoryMapper.selectById(courseBase.getSt()).getName()
        );

        return courseBaseInfoDto;
    }

    @Transactional
    @Override
    public CourseBaseInfoDto modifyCourseBase(Long companyId, EditCourseDto dto) {
        // 1. 修改的要求：课程id与机构id都要一致，才能修改
        LambdaQueryWrapper<CourseBase> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseBase::getId, dto.getId())
                .eq(CourseBase::getCompanyId, companyId);

        // 2. 封装课程基本信息的数据
        CourseBase courseBase = new CourseBase();
        BeanUtils.copyProperties(dto, courseBase);
        courseBase.setChangeDate(LocalDateTime.now());

        // 3. 更新课程基本信息
        int res1 = courseBaseMapper.update(courseBase, wrapper);
        if (res1 <= 0) {
            XueTangException.cast("更新课程失败");
        }

        // 4. 封装课程营销信息的数据
        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(dto, courseMarket);

        // 5. 更新课程营销信息
        saveCourseMarket(courseMarket);

        // 6. 组装返回结果
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        // 赋值课程基本信息
        BeanUtils.copyProperties(courseBase, courseBaseInfoDto);
        // 赋值课程营销信息
        BeanUtils.copyProperties(courseMarket, courseBaseInfoDto);
        // 查询分类名称，并赋值
        courseBaseInfoDto.setMtName(
            courseCategoryMapper.selectById(courseBase.getMt()).getName()
        );
        courseBaseInfoDto.setStName(
            courseCategoryMapper.selectById(courseBase.getSt()).getName()
        );

        return courseBaseInfoDto;
    }

    @Transactional
    @Override
    public void deleteCourse(Long companyId, Long courseId) {
        // 1. 删除课程基本信息。注意，审核状态为未提交，且为本机构的课程，才可以删除
        LambdaQueryWrapper<CourseBase> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseBase::getAuditStatus, "202002")
                .eq(CourseBase::getCompanyId, companyId)
                .eq(CourseBase::getId, courseId);
        int res = courseBaseMapper.delete(wrapper);
        if (res <= 0) {
            XueTangException.cast("只允许删除本机构的未提交课程");
        }

        // 2. 删除课程营销信息
        courseMarketMapper.deleteById(courseId);

        // 3. 删除教学计划信息
        LambdaQueryWrapper<Teachplan> planWrapper = new LambdaQueryWrapper<>();
        planWrapper.eq(Teachplan::getCourseId, courseId);
        teachplanMapper.delete(planWrapper);

        // 4. 删除课程教师信息
        LambdaQueryWrapper<CourseTeacher> teacherWrapper = new LambdaQueryWrapper<>();
        teacherWrapper.eq(CourseTeacher::getCourseId, courseId);
        courseTeacherMapper.delete(teacherWrapper);
    }
}




