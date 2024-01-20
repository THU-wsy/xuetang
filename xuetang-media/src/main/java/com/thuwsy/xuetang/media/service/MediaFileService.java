package com.thuwsy.xuetang.media.service;

import com.thuwsy.xuetang.base.model.PageParams;
import com.thuwsy.xuetang.base.model.PageResult;
import com.thuwsy.xuetang.base.model.RestResponse;
import com.thuwsy.xuetang.media.dto.QueryMediaParamsDto;
import com.thuwsy.xuetang.media.dto.UploadFileParamsDto;
import com.thuwsy.xuetang.media.po.MediaFiles;

/**
 * ClassName: MediaFileService
 * Package: com.thuwsy.xuetang.media.service
 * Description: 媒资文件管理业务类
 *
 * @Author THU_wsy
 * @Create 2024/1/19 17:35
 * @Version 1.0
 */
public interface MediaFileService {

    /**
     * 媒资文件查询
     * @param companyId 机构id
     * @param params 分页参数
     * @param dto 查询条件
     * @return PageResult<MediaFiles>
     */
    PageResult<MediaFiles> queryMediaFiles(Long companyId, PageParams params, QueryMediaParamsDto dto);

    /**
     * 上传文件的通用接口
     * @param companyId 机构id
     * @param dto 文件信息
     * @param bytes 文件的字节数组
     * @return
     */
    MediaFiles uploadFile(Long companyId, UploadFileParamsDto dto, byte[] bytes);

    /**
     * 上传文件到数据库
     */
    MediaFiles addMediaFilesToDB(Long companyId, UploadFileParamsDto dto,
                                 String objectName, String fileMD5,
                                 String bucketName, long fileSize);

    /**
     * 检查文件是否存在
     */
    RestResponse<Boolean> checkFile(String fileMd5);

    /**
     * 检查分块文件是否存在
     */
    RestResponse<Boolean> checkChunk(String fileMd5, int chunkIndex);

    /**
     * 上传分块文件
     */
    RestResponse<Boolean> uploadChunk(String fileMd5, int chunk, byte[] bytes);

    /**
     * 合并分块文件
     */
    RestResponse<Boolean> mergeChunks(Long companyId, String fileMd5, int chunkTotal, UploadFileParamsDto uploadFileParamsDto);
}
