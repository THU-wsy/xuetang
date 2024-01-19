package com.thuwsy.xuetang.content.dto;

import com.thuwsy.xuetang.content.po.Teachplan;
import com.thuwsy.xuetang.content.po.TeachplanMedia;
import lombok.Data;

import java.util.List;

/**
 * ClassName: TeachplanDto
 * Package: com.thuwsy.xuetang.content.dto
 * Description: 教学计划树形结构DTO
 *
 * @Author THU_wsy
 * @Create 2024/1/17 21:36
 * @Version 1.0
 */
@Data
public class TeachplanDto extends Teachplan {

    /**
     * 教学计划关联的媒资信息
     */
    private TeachplanMedia teachplanMedia;

    /**
     * 子节点
     */
    private List<TeachplanDto> teachPlanTreeNodes;
}
