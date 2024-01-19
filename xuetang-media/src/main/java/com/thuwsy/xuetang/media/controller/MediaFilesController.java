package com.thuwsy.xuetang.media.controller;

import com.thuwsy.xuetang.base.model.PageParams;
import com.thuwsy.xuetang.base.model.PageResult;
import com.thuwsy.xuetang.media.dto.QueryMediaParamsDto;
import com.thuwsy.xuetang.media.po.MediaFiles;
import com.thuwsy.xuetang.media.service.MediaFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: MediaFilesController
 * Package: com.thuwsy.xuetang.media.controller
 * Description: 媒资文件管理controller
 *
 * @Author THU_wsy
 * @Create 2024/1/19 18:32
 * @Version 1.0
 */
@RestController
@RequestMapping("/media/files")
public class MediaFilesController {
    @Autowired
    private MediaFileService mediaFileService;

    @PostMapping
    public PageResult<MediaFiles> list(PageParams params, @RequestBody QueryMediaParamsDto dto) {
        Long companyId = 1232141425L;
        return mediaFileService.queryMediaFiles(companyId, params, dto);
    }
}
