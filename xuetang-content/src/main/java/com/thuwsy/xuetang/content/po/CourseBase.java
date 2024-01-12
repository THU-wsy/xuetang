package com.thuwsy.xuetang.content.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName course_base
 */
@TableName(value ="course_base")
@Data
public class CourseBase implements Serializable {
    private Long id;

    private Long companyId;

    private String companyName;

    private String name;

    private String users;

    private String tags;

    private String mt;

    private String st;

    private String grade;

    private String teachmode;

    private String description;

    private String pic;

    private Date createDate;

    private Date changeDate;

    private String createPeople;

    private String changePeople;

    private String auditStatus;

    private String status;

    private static final long serialVersionUID = 1L;
}