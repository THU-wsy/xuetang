package com.thuwsy.xuetang.content.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName mq_message
 */
@TableName(value ="mq_message")
@Data
public class MqMessage implements Serializable {
    private Long id;

    private String messageType;

    private String businessKey1;

    private String businessKey2;

    private String businessKey3;

    private Integer executeNum;

    private String state;

    private Date returnfailureDate;

    private Date returnsuccessDate;

    private String returnfailureMsg;

    private Date executeDate;

    private String stageState1;

    private String stageState2;

    private String stageState3;

    private String stageState4;

    private static final long serialVersionUID = 1L;
}