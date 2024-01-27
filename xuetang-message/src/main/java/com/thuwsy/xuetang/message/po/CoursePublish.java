package com.thuwsy.xuetang.message.po;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * ClassName: CoursePublish
 * Package: com.thuwsy.xuetang.message.po
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/1/24 14:25
 * @Version 1.0
 */
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

    private LocalDateTime createDate;

    private LocalDateTime onlineDate;

    private LocalDateTime offlineDate;

    private String status;

    private String remark;

    private String charge;

    private Double price;

    private Double originalPrice;

    private Integer validDays;

    private static final long serialVersionUID = 1L;
}
