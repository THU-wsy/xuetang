package com.thuwsy.xuetang.auth.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName xc_user_role
 */
@TableName(value ="xc_user_role")
@Data
public class XcUserRole implements Serializable {
    /**
     * 
     */
    @TableId
    private String id;

    /**
     * 
     */
    private String userId;

    /**
     * 
     */
    private String roleId;

    /**
     * 
     */
    private LocalDateTime createTime;

    /**
     * 
     */
    private String creator;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}