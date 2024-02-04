package com.thuwsy.xuetang.auth.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName xc_company
 */
@TableName(value ="xc_company")
@Data
public class XcCompany implements Serializable {
    /**
     * 
     */
    @TableId
    private String id;

    /**
     * 联系人名称
     */
    private String linkname;

    /**
     * 名称
     */
    private String name;

    /**
     * 
     */
    private String mobile;

    /**
     * 
     */
    private String email;

    /**
     * 简介
     */
    private String intro;

    /**
     * logo
     */
    private String logo;

    /**
     * 身份证照片
     */
    private String identitypic;

    /**
     * 工具性质
     */
    private String worktype;

    /**
     * 营业执照
     */
    private String businesspic;

    /**
     * 企业状态
     */
    private String status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}