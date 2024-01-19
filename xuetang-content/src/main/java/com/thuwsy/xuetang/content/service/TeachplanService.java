package com.thuwsy.xuetang.content.service;

import com.thuwsy.xuetang.content.dto.SaveTeachplanDto;
import com.thuwsy.xuetang.content.dto.TeachplanDto;
import com.thuwsy.xuetang.content.po.Teachplan;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 吴晟宇
* @description 针对表【teachplan(课程计划)】的数据库操作Service
* @createDate 2024-01-12 14:44:11
*/
public interface TeachplanService extends IService<Teachplan> {

    /**
     * 查询教学计划树形结构
     * @param courseId 课程id
     * @return List<TeachplanDto>
     */
    List<TeachplanDto> findTeachplanTree(Long courseId);

    /**
     * 新增/修改教学计划
     */
    void saveTeachplan(SaveTeachplanDto dto);

    /**
     * 删除教学计划
     */
    void deleteTeachplan(Long id);

    /**
     * 下移课程章节
     */
    void moveDownTeachplan(Long id);

    /**
     * 上移课程章节
     */
    void moveUpTeachplan(Long id);
}
