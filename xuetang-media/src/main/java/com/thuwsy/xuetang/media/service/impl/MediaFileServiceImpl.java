package com.thuwsy.xuetang.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thuwsy.xuetang.base.model.PageParams;
import com.thuwsy.xuetang.base.model.PageResult;
import com.thuwsy.xuetang.media.dto.QueryMediaParamsDto;
import com.thuwsy.xuetang.media.mapper.MediaFilesMapper;
import com.thuwsy.xuetang.media.po.MediaFiles;
import com.thuwsy.xuetang.media.service.MediaFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ClassName: MediaFileServiceImpl
 * Package: com.thuwsy.xuetang.media.service.impl
 * Description: 媒资文件管理业务实现类
 *
 * @Author THU_wsy
 * @Create 2024/1/19 17:37
 * @Version 1.0
 */
@Service
public class MediaFileServiceImpl implements MediaFileService {

    @Autowired
    private MediaFilesMapper mediaFilesMapper;

    @Override
    public PageResult<MediaFiles> queryMediaFiles(Long companyId, PageParams params, QueryMediaParamsDto dto) {
        // 1. 分页查询
        LambdaQueryWrapper<MediaFiles> wrapper = new LambdaQueryWrapper<>();
        Page<MediaFiles> page = new Page<>(params.getPageNo(), params.getPageSize());
        Page<MediaFiles> pageResult = mediaFilesMapper.selectPage(page, wrapper);

        // 2. 获取数据列表
        List<MediaFiles> list = pageResult.getRecords();
        // 3. 获取数据总数
        long total = pageResult.getTotal();

        // 4. 返回结果
        return new PageResult<>(list, total,
                params.getPageNo(), params.getPageSize());
    }
}
