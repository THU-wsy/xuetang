package com.thuwsy.xuetang.media.controller;

import com.thuwsy.xuetang.base.exception.XueTangException;
import com.thuwsy.xuetang.base.model.RestResponse;
import com.thuwsy.xuetang.media.dto.UploadFileParamsDto;
import com.thuwsy.xuetang.media.service.MediaFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * ClassName: BigFilesController
 * Package: com.thuwsy.xuetang.media.controller
 * Description: 大文件上传接口
 *
 * @Author THU_wsy
 * @Create 2024/1/20 20:50
 * @Version 1.0
 */
@RestController
@RequestMapping("/media")
public class BigFilesController {

    @Autowired
    private MediaFileService mediaFileService;

    /**
     * 文件上传前，先检查文件是否存在
     */
    @PostMapping("/upload/checkfile")
    public RestResponse<Boolean> checkFile(@RequestParam("fileMd5") String fileMd5) {
        return mediaFileService.checkFile(fileMd5);
    }

    /**
     * 分块文件上传前，先检查该分块文件是否存在
     */
    @PostMapping("/upload/checkchunk")
    public RestResponse<Boolean> checkChunk(@RequestParam("fileMd5") String fileMd5,
                                            @RequestParam("chunk") int chunk) {
        return mediaFileService.checkChunk(fileMd5, chunk);
    }

    /**
     * 上传分块文件
     */
    @PostMapping("/upload/uploadchunk")
    public RestResponse<Boolean> uploadChunk(@RequestParam("file") MultipartFile file,
                                    @RequestParam("fileMd5") String fileMd5,
                                    @RequestParam("chunk") int chunk) {
        try {
            return mediaFileService.uploadChunk(fileMd5, chunk, file.getBytes());
        } catch (IOException e) {
            XueTangException.cast("上传分块文件失败！");
        }
        return RestResponse.validfail("上传文件失败", false);
    }

    /**
     * 合并分块文件
     */
    @PostMapping("/upload/mergechunks")
    public RestResponse<Boolean> mergeChunks(@RequestParam("fileMd5") String fileMd5,
                                    @RequestParam("fileName") String fileName,
                                    @RequestParam("chunkTotal") int chunkTotal) {
        Long companyId = 1232141425L;
        UploadFileParamsDto uploadFileParamsDto = new UploadFileParamsDto();
        uploadFileParamsDto.setFileType("001002");
        uploadFileParamsDto.setTags("课程视频");
        uploadFileParamsDto.setRemark("");
        uploadFileParamsDto.setFilename(fileName);
        return mediaFileService.mergeChunks(companyId, fileMd5, chunkTotal, uploadFileParamsDto);
    }
}