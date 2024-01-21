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

import java.io.File;
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

    /**
     * 媒资文件分页查询
     */
    @PostMapping("/files")
    public PageResult<MediaFiles> list(PageParams params, @RequestBody QueryMediaParamsDto dto) {
        Long companyId = 1232141425L;
        return mediaFileService.queryMediaFiles(companyId, params, dto);
    }

    /**
     * 上传普通文件的接口
     */
    @RequestMapping("/upload/coursefile")
    public MediaFiles uploadFile(@RequestPart("filedata") MultipartFile file) {
        Long companyId = 1232141425L;

        // 1. 封装文件上传请求DTO
        UploadFileParamsDto dto = new UploadFileParamsDto();
        dto.setFileType("001001"); // 文件类型统一设置为图片
        dto.setTags("");
        dto.setRemark("");
        dto.setFilename(file.getOriginalFilename());
        dto.setFileSize(file.getSize());

        File tmp = null;
        try {
            // 2. 将文件保存到本地的临时文件（否则通过网络传输的文件MD5值可能不同）
            // 创建临时文件
            tmp = File.createTempFile("minio", ".tmp");
            // 将上传的文件拷贝到临时文件
            file.transferTo(tmp);
            // 获取临时文件的路径
            String localFilePath = tmp.getAbsolutePath();

            // 3. 调用service进行文件上传
            MediaFiles response = mediaFileService.uploadFile(companyId, dto, localFilePath);

            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 4. 删除本地的临时文件
            tmp.delete();
        }
    }
}
