package com.thuwsy.xuetang.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thuwsy.xuetang.base.exception.XueTangException;
import com.thuwsy.xuetang.content.dto.BindTeachplanMediaDto;
import com.thuwsy.xuetang.content.mapper.TeachplanMapper;
import com.thuwsy.xuetang.content.po.Teachplan;
import com.thuwsy.xuetang.content.po.TeachplanMedia;
import com.thuwsy.xuetang.content.service.TeachplanMediaService;
import com.thuwsy.xuetang.content.mapper.TeachplanMediaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
* @author 吴晟宇
* @description 针对表【teachplan_media】的数据库操作Service实现
* @createDate 2024-01-12 14:44:11
*/
@Service
public class TeachplanMediaServiceImpl extends ServiceImpl<TeachplanMediaMapper, TeachplanMedia>
    implements TeachplanMediaService{

    @Autowired
    private TeachplanMediaMapper teachplanMediaMapper;

    @Autowired
    private TeachplanMapper teachplanMapper;

    @Transactional
    @Override
    public void associationMedia(BindTeachplanMediaDto dto) {
        // 1. 获取教学计划id
        Long teachplanId = dto.getTeachplanId();

        // 2. 如果该教学计划不存在，则抛出异常
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        if (teachplan == null) {
            XueTangException.cast("该教学计划不存在");
        }

        // 3. 如果该教学计划已经与某个媒资信息绑定，则进行删除
        LambdaQueryWrapper<TeachplanMedia> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TeachplanMedia::getTeachplanId, teachplanId);
        teachplanMediaMapper.delete(wrapper);

        // 4. 添加教学计划和媒资信息的绑定关系
        TeachplanMedia teachplanMedia = new TeachplanMedia();
        teachplanMedia.setMediaId(dto.getMediaId());
        teachplanMedia.setTeachplanId(teachplanId);
        teachplanMedia.setCourseId(teachplan.getCourseId());
        teachplanMedia.setMediaFilename(dto.getFileName());
        teachplanMedia.setCreateDate(LocalDateTime.now());

        teachplanMediaMapper.insert(teachplanMedia);
    }

    @Transactional
    @Override
    public void deleteAssociationMedia(Long teachPlanId, String mediaId) {
        LambdaQueryWrapper<TeachplanMedia> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TeachplanMedia::getTeachplanId, teachPlanId)
                .eq(TeachplanMedia::getMediaId, mediaId);
        teachplanMediaMapper.delete(wrapper);
    }
}




