package com.thuwsy.xuetang.content.dto;

import com.thuwsy.xuetang.content.po.CourseBase;
import lombok.Data;

/**
 * ClassName: CourseBaseInfoDto
 * Package: com.thuwsy.xuetang.content.dto
 * Description: 课程基本信息DTO
 *
 * @Author THU_wsy
 * @Create 2024/1/17 12:36
 * @Version 1.0
 */
@Data
public class CourseBaseInfoDto extends CourseBase {
    /**
     * 收费规则，对应数据字典
     */
    private String charge;

    /**
     * 价格
     */
    private Double price;

    /**
     * 原价
     */
    private Double originalPrice;

    /**
     * 咨询qq
     */
    private String qq;

    /**
     * 微信
     */
    private String wechat;

    /**
     * 电话
     */
    private String phone;

    /**
     * 有效期天数
     */
    private Integer validDays;

    /**
     * 大分类名称
     */
    private String mtName;

    /**
     * 小分类名称
     */
    private String stName;
}
