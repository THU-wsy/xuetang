package com.thuwsy.xuetang.content.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * ClassName: SaveTeachplanDto
 * Package: com.thuwsy.xuetang.content.dto
 * Description: 新增/修改课程计划DTO
 *
 * @Author THU_wsy
 * @Create 2024/1/18 10:12
 * @Version 1.0
 */
@Data
public class SaveTeachplanDto {

    /**
     * 教学计划id
     */
    private Long id;

    /**
     * 章节名称
     */
    @NotBlank(message = "章节名称不能为空")
    private String pname;

    /**
     * 父级id
     */
    @NotNull(message = "父级id不能为空")
    private Long parentid;

    /**
     * 层级（分为1级和2级）
     */
    @NotNull(message = "层级不能为空")
    private Integer grade;

    /**
     * 课程媒资类型（视频/文档）
     */
    private String mediaType;

    /**
     * 课程id
     */
    @NotNull(message = "课程id不能为空")
    private Long courseId;

    /**
     * 课程发布id
     */
    private Long coursePubId;

    /**
     * 是否支持预览（试看）
     */
    private String isPreview;
}
