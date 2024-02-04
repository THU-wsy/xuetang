package com.thuwsy.xuetang.auth.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName xc_company_user
 */
@TableName(value ="xc_company_user")
@Data
public class XcCompanyUser implements Serializable {
    /**
     * 
     */
    @TableId
    private String id;

    /**
     * 
     */
    private String companyId;

    /**
     * 
     */
    private String userId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}