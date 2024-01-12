package com.thuwsy.xuetang.content.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName course_market
 */
@TableName(value ="course_market")
@Data
public class CourseMarket implements Serializable {
    private Long id;

    private String charge;

    private Double price;

    private Double originalPrice;

    private String qq;

    private String wechat;

    private String phone;

    private Integer validDays;

    private static final long serialVersionUID = 1L;
}