package com.thuwsy.xuetang.auth.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName xc_teacher
 */
@TableName(value ="xc_teacher")
@Data
public class XcTeacher implements Serializable {
    /**
     * 
     */
    @TableId
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 称呼
     */
    private String name;

    /**
     * 个人简介
     */
    private String intro;

    /**
     * 个人简历
     */
    private String resume;

    /**
     * 老师照片
     */
    private String pic;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}