package com.thuwsy.xuetang.content.dto;

import lombok.Data;

/**
 * ClassName: BindTeachplanMediaDto
 * Package: com.thuwsy.xuetang.content.dto
 * Description: 教学计划-媒资绑定DTO
 *
 * @Author THU_wsy
 * @Create 2024/1/22 13:30
 * @Version 1.0
 */
@Data
public class BindTeachplanMediaDto {
    /**
     * 媒资文件id
     */
    private String mediaId;

    /**
     * 媒资文件名称
     */
    private String fileName;

    /**
     * 教学计划id
     */
    private Long teachplanId;
}
