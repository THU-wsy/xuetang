package com.thuwsy.xuetang.content.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @TableName teachplan_media
 */
@TableName(value ="teachplan_media")
@Data
public class TeachplanMedia implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String mediaId;

    private Long teachplanId;

    private Long courseId;

    private String mediaFilename;

    private LocalDateTime createDate;

    private String createPeople;

    private String changePeople;

    private static final long serialVersionUID = 1L;
}