package com.thuwsy.xuetang.media.controller;

import com.thuwsy.xuetang.base.exception.XueTangException;
import com.thuwsy.xuetang.base.model.PageParams;
import com.thuwsy.xuetang.base.model.PageResult;
import com.thuwsy.xuetang.media.dto.QueryMediaParamsDto;
import com.thuwsy.xuetang.media.dto.UploadFileParamsDto;
import com.thuwsy.xuetang.media.po.MediaFiles;
import com.thuwsy.xuetang.media.service.MediaFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
@RequestMapping("/media")
public class MediaFilesController {
    @Autowired
    private MediaFileService mediaFileService;

    @PostMapping("/files")
    public PageResult<MediaFiles> list(PageParams params, @RequestBody QueryMediaParamsDto dto) {
        Long companyId = 1232141425L;
        return mediaFileService.queryMediaFiles(companyId, params, dto);
    }

    /**
     * 文件上传
     */
    @RequestMapping("/upload/coursefile")
    public MediaFiles upload(@RequestPart("filedata") MultipartFile file) {
        // 封装文件上传请求DTO
        UploadFileParamsDto dto = new UploadFileParamsDto();
//        String contentType = file.getContentType();
        dto.setFileType("001001"); // 图片
        dto.setFilename(file.getOriginalFilename());
//        dto.setContentType(contentType);

        Long companyId = 1232141425L;

        try {
            return mediaFileService.uploadFile(companyId, dto, file.getBytes());
        } catch (Exception e) {
            XueTangException.cast("上传文件失败");
        }
        return null;
    }
}
