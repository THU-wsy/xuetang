package com.thuwsy.xuetang.content.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName course_publish
 */
@TableName(value ="course_publish")
@Data
public class CoursePublish implements Serializable {
    private Long id;

    private Long companyId;

    private String companyName;

    private String name;

    private String users;

    private String tags;

    private String username;

    private String mt;

    private String mtName;

    private String st;

    private String stName;

    private String grade;

    private String teachmode;

    private String pic;

    private String description;

    private String market;

    private String teachplan;

    private String teachers;

    private Date createDate;

    private Date onlineDate;

    private Date offlineDate;

    private String status;

    private String remark;

    private String charge;

    private Double price;

    private Double originalPrice;

    private Integer validDays;

    private static final long serialVersionUID = 1L;
}