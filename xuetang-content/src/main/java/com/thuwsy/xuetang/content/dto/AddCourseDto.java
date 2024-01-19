package com.thuwsy.xuetang.content.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * ClassName: AddCourseDto
 * Package: com.thuwsy.xuetang.content.dto
 * Description: 添加课程DTO
 *
 * @Author THU_wsy
 * @Create 2024/1/17 12:27
 * @Version 1.0
 */
@Data
public class AddCourseDto {

    @NotBlank(message = "课程名称不能为空")
    private String name;

    @NotBlank(message = "适用人群不能为空")
    @Length(message = "适用人群太少", min = 10)
    private String users;

    private String tags;

    @NotBlank(message = "课程分类不能为空")
    private String mt;

    @NotBlank(message = "课程分类不能为空")
    private String st;

    @NotBlank(message = "课程等级不能为空")
    private String grade;

    @NotBlank(message = "教育模式不能为空")
    private String teachmode;

    private String description;

    private String pic;

    @NotBlank(message = "收费规则不能为空")
    private String charge;

    private Double price;

    private Double originalPrice;

    private String qq;

    private String wechat;

    private String phone;

    private Integer validDays;
}
