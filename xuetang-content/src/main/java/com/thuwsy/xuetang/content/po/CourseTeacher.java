package com.thuwsy.xuetang.content.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @TableName course_teacher
 */
@TableName(value ="course_teacher")
@Data
public class CourseTeacher implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long courseId;

    private String teacherName;

    private String position;

    private String introduction;

    private String photograph;

    private LocalDateTime createDate;

    private static final long serialVersionUID = 1L;
}