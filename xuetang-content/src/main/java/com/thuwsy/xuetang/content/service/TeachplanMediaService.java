package com.thuwsy.xuetang.content.service;

import com.thuwsy.xuetang.content.dto.BindTeachplanMediaDto;
import com.thuwsy.xuetang.content.po.TeachplanMedia;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 吴晟宇
* @description 针对表【teachplan_media】的数据库操作Service
* @createDate 2024-01-12 14:44:11
*/
public interface TeachplanMediaService extends IService<TeachplanMedia> {

    /**
     * 绑定教学计划和媒资信息
     */
    void associationMedia(BindTeachplanMediaDto dto);

    /**
     * 解绑教学计划和媒资信息
     */
    void deleteAssociationMedia(Long teachPlanId, String mediaId);
}
