package com.thuwsy.xuetang.media.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.thuwsy.xuetang.base.exception.XueTangException;
import com.thuwsy.xuetang.base.model.RestResponse;
import com.thuwsy.xuetang.media.po.MediaFiles;
import com.thuwsy.xuetang.media.service.MediaFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: MediaOpenController
 * Package: com.thuwsy.xuetang.media.controller
 * Description: 媒资文件公开接口
 *
 * @Author THU_wsy
 * @Create 2024/1/22 21:39
 * @Version 1.0
 */
@RestController
@RequestMapping("/media/open")
public class MediaOpenController {
    @Autowired
    private MediaFileService mediaFileService;

    @GetMapping("/preview/{mediaId}")
    public RestResponse<String> getPlayUrlByMediaId(@PathVariable String mediaId) {
        MediaFiles mediaFiles = mediaFileService.getFileById(mediaId);
        if (mediaFiles == null || StringUtils.isEmpty(mediaFiles.getUrl())) {
            XueTangException.cast("视频地址不存在");
        }
        return RestResponse.success(mediaFiles.getUrl());
    }
}
