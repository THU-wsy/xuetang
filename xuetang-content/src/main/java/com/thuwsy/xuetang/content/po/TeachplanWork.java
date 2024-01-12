package com.thuwsy.xuetang.content.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName teachplan_work
 */
@TableName(value ="teachplan_work")
@Data
public class TeachplanWork implements Serializable {
    private Long id;

    private Long workId;

    private String workTitle;

    private Long teachplanId;

    private Long courseId;

    private Date createDate;

    private Long coursePubId;

    private static final long serialVersionUID = 1L;
}