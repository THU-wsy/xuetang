package com.thuwsy.xuetang.media.dto;

import lombok.Data;

/**
 * ClassName: UploadFileParamsDto
 * Package: com.thuwsy.xuetang.media.dto
 * Description: 封装文件上传请求DTO
 *
 * @Author THU_wsy
 * @Create 2024/1/20 13:08
 * @Version 1.0
 */
@Data
public class UploadFileParamsDto {

    /**
     * 文件名称
     */
    private String filename;

    /**
     * 文件content-type
     */
    private String contentType;

    /**
     * 文件类型（文档，图片，视频）
     */
    private String fileType;
    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 标签
     */
    private String tags;

    /**
     * 上传人
     */
    private String username;

    /**
     * 备注
     */
    private String remark;
}
