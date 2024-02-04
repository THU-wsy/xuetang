package com.thuwsy.xuetang.content.controller;

import com.thuwsy.xuetang.base.model.PageParams;
import com.thuwsy.xuetang.base.model.PageResult;
import com.thuwsy.xuetang.content.dto.AddCourseDto;
import com.thuwsy.xuetang.content.dto.CourseBaseInfoDto;
import com.thuwsy.xuetang.content.dto.EditCourseDto;
import com.thuwsy.xuetang.content.dto.QueryCourseParamsDto;
import com.thuwsy.xuetang.content.po.CourseBase;
import com.thuwsy.xuetang.content.po.XcUser;
import com.thuwsy.xuetang.content.service.CourseBaseService;
import com.thuwsy.xuetang.content.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: CourseBaseController
 * Package: com.thuwsy.xuetang.content.controller
 * Description: 课程信息controller
 *
 * @Author THU_wsy
 * @Create 2024/1/12 14:48
 * @Version 1.0
 */
@RestController
@RequestMapping("/content/course")
public class CourseBaseController {
    @Autowired
    private CourseBaseService courseBaseService;

    @PreAuthorize("hasAuthority('xc_teachmanager_course_list')")
    @PostMapping("/list")
    public PageResult<CourseBase> list(PageParams params,
            @RequestBody(required = false) QueryCourseParamsDto dto) {
        XcUser xcUser = SecurityUtil.getXcUser();
        String companyIdString = xcUser.getCompanyId();
        Long companyId = (companyIdString == null) ? null : Long.valueOf(companyIdString);
        return courseBaseService.queryCourseBaseList(companyId, params, dto);
    }

    /**
     * 新增课程信息，需要使用@Validated进行参数校验
     */
    @PostMapping
    public CourseBaseInfoDto createCourseBase(@Validated @RequestBody AddCourseDto dto) {
        // 机构id，由于认证系统没有上线暂时硬编码
        Long companyId = 1232141425L;

        return courseBaseService.createCourseBase(companyId, dto);
    }

    /**
     * 根据课程id查询课程信息
     */
    @GetMapping("/{id}")
    public CourseBaseInfoDto getCourseBaseById(@PathVariable Long id) {
        XcUser xcUser = SecurityUtil.getXcUser();
        System.out.println(xcUser);
        return courseBaseService.getCourseBaseById(id);
    }

    /**
     * 修改课程信息
     */
    @PutMapping
    public CourseBaseInfoDto modifyCourseBase(@Validated @RequestBody EditCourseDto dto) {
        // 机构id，由于认证系统没有上线暂时硬编码
        Long companyId = 1232141425L;

        return courseBaseService.modifyCourseBase(companyId, dto);
    }

    /**
     * 删除课程（只能删除本机构的课程）
     */
    @DeleteMapping("/{courseId}")
    public void deleteCourse(@PathVariable Long courseId) {
        Long companyId = 1232141425L;
        courseBaseService.deleteCourse(companyId, courseId);
    }
}
