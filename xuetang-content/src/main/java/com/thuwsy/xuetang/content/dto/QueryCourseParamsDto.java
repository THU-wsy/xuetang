package com.thuwsy.xuetang.content.dto;

import lombok.Data;

/**
 * ClassName: QueryCourseParamsDto
 * Package: com.thuwsy.xuetang.content.dto
 * Description: 课程查询参数DTO
 *
 * @Author THU_wsy
 * @Create 2024/1/12 15:02
 * @Version 1.0
 */
@Data
public class QueryCourseParamsDto {
    /** 审核状态 */
    private String auditStatus;

    /** 课程名称 */
    private String courseName;

    /** 发布状态 */
    private String publishStatus;
}
