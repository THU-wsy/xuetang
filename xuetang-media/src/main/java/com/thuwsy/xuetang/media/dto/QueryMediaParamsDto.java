package com.thuwsy.xuetang.media.dto;

import lombok.Data;

/**
 * ClassName: QueryMediaParamsDto
 * Package: com.thuwsy.xuetang.media.dto
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/1/19 17:21
 * @Version 1.0
 */
@Data
public class QueryMediaParamsDto {

    /**
     * 媒资文件名称
     */
    private String filename;

    /**
     * 媒资类型
     */
    private String fileType;

    /**
     * 审核状态
     */
    private String auditStatus;
}
