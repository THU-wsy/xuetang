package com.thuwsy.xuetang.media.service;

import com.thuwsy.xuetang.base.model.PageParams;
import com.thuwsy.xuetang.base.model.PageResult;
import com.thuwsy.xuetang.media.dto.QueryMediaParamsDto;
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
}
