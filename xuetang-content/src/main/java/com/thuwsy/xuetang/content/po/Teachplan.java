package com.thuwsy.xuetang.content.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @TableName teachplan
 */
@TableName(value ="teachplan")
@Data
public class Teachplan implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String pname;

    private Long parentid;

    private Integer grade;

    private String mediaType;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String description;

    private String timelength;

    private Integer orderby;

    private Long courseId;

    private Long coursePubId;

    private Integer status;

    private String isPreview;

    private LocalDateTime createDate;

    private LocalDateTime changeDate;

    private static final long serialVersionUID = 1L;
}