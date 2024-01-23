package com.thuwsy.xuetang.content.controller;

import com.thuwsy.xuetang.content.dto.BindTeachplanMediaDto;
import com.thuwsy.xuetang.content.service.TeachplanMediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: TeachplanMediaController
 * Package: com.thuwsy.xuetang.content.controller
 * Description: 绑定教学计划和媒资信息的controller
 *
 * @Author THU_wsy
 * @Create 2024/1/22 13:36
 * @Version 1.0
 */
@RestController
@RequestMapping("/content/teachplan")
public class TeachplanMediaController {
    @Autowired
    private TeachplanMediaService teachplanMediaService;

    /**
     * 绑定教学计划和媒资信息
     */
    @PostMapping("/association/media")
    public void associationMedia(@RequestBody BindTeachplanMediaDto dto) {
        teachplanMediaService.associationMedia(dto);
    }

    /**
     * 解绑教学计划和媒资信息
     */
    @DeleteMapping("/association/media/{teachPlanId}/{mediaId}")
    public void deleteAssociationMedia(@PathVariable Long teachPlanId, @PathVariable String mediaId) {
        teachplanMediaService.deleteAssociationMedia(teachPlanId, mediaId);
    }
}
