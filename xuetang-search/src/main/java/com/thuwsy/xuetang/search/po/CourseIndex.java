package com.thuwsy.xuetang.search.po;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * ClassName: CourseIndex
 * Package: com.thuwsy.xuetang.search.po
 * Description: 课程文档
 *
 * @Author THU_wsy
 * @Create 2024/1/24 22:25
 * @Version 1.0
 */
@Data
public class CourseIndex implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 机构ID
     */
    private Long companyId;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 课程名称
     */
    private String name;

    /**
     * 适用人群
     */
    private String users;

    /**
     * 标签
     */
    private String tags;

    /**
     * 大分类
     */
    private String mt;

    /**
     * 大分类名称
     */
    private String mtName;

    /**
     * 小分类
     */
    private String st;

    /**
     * 小分类名称
     */
    private String stName;


    /**
     * 课程等级
     */
    private String grade;

    /**
     * 教育模式
     */
    private String teachmode;

    /**
     * 课程图片
     */
    private String pic;

    /**
     * 课程介绍
     */
    private String description;


    /**
     * 发布时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    /**
     * 状态
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 收费规则，对应数据字典--203
     */
    private String charge;

    /**
     * 现价
     */
    private Double price;

    /**
     * 原价
     */
    private Double originalPrice;

    /**
     * 课程有效期天数
     */
    private Integer validDays;
}
