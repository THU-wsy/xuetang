package com.thuwsy.xuetang.content.dto;

import lombok.Data;

import java.util.List;

/**
 * ClassName: CoursePreviewDto
 * Package: com.thuwsy.xuetang.content.dto
 * Description: 预览课程的数据模型
 *
 * @Author THU_wsy
 * @Create 2024/1/22 21:13
 * @Version 1.0
 */
@Data
public class CoursePreviewDto {

    /**
     * 课程基本信息、课程营销信息
     */
    private CourseBaseInfoDto courseBase;

    /**
     * 课程计划信息
     */
    List<TeachplanDto> teachplans;

    /**
     * 师资信息（之后再加）
     */

}
