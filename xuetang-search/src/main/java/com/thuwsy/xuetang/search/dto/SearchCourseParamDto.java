package com.thuwsy.xuetang.search.dto;

import lombok.Data;

/**
 * ClassName: SearchCourseParamDto
 * Package: com.thuwsy.xuetang.search.dto
 * Description: 搜索课程参数DTO
 *
 * @Author THU_wsy
 * @Create 2024/1/24 22:28
 * @Version 1.0
 */
@Data
public class SearchCourseParamDto {
    // 关键字
    private String keywords;

    // 大分类
    private String mt;

    // 小分类
    private String st;

    // 难度等级
    private String grade;
}
