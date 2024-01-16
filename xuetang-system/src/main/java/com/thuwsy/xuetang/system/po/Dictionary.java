package com.thuwsy.xuetang.system.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * ClassName: Dictionary
 * Package: com.thuwsy.xuetang.system.po
 * Description: 数据字典
 *
 * @Author THU_wsy
 * @Create 2024/1/16 20:55
 * @Version 1.0
 */
@Data
@TableName("dictionary")
public class Dictionary implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 数据字典名称
     */
    private String name;

    /**
     * 数据字典代码
     */
    private String code;

    /**
     * 数据字典项-json格式
     */
    private String itemValues;
}
