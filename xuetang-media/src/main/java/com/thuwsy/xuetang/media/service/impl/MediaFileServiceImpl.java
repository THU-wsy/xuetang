package com.thuwsy.xuetang.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import com.thuwsy.xuetang.base.exception.XueTangException;
import com.thuwsy.xuetang.base.model.PageParams;
import com.thuwsy.xuetang.base.model.PageResult;
import com.thuwsy.xuetang.base.model.RestResponse;
import com.thuwsy.xuetang.media.dto.QueryMediaParamsDto;
import com.thuwsy.xuetang.media.dto.UploadFileParamsDto;
import com.thuwsy.xuetang.media.mapper.MediaFilesMapper;
import com.thuwsy.xuetang.media.po.MediaFiles;
import com.thuwsy.xuetang.media.service.MediaFileService;
import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
@Slf4j
@Service
public class MediaFileServiceImpl implements MediaFileService {

    @Autowired
    private MediaFilesMapper mediaFilesMapper;

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket.mediafiles}")
    private String bucketFiles;

    @Value("${minio.bucket.video}")
    private String bucketVideo;

    @Lazy // 使用该注解，延迟加载，防止循环依赖
    @Autowired
    private MediaFileService mediaFileService; // 代理对象，用于控制事务

    /**
     * 媒资文件分页查询
     * @param companyId 机构id
     * @param params 分页参数
     * @param dto 查询条件
     */
    @Override
    public PageResult<MediaFiles> queryMediaFiles(Long companyId, PageParams params, QueryMediaParamsDto dto) {
        // 1. 分页查询
        LambdaQueryWrapper<MediaFiles> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(dto.getFilename()), MediaFiles::getFilename, dto.getFilename())
                .eq(StringUtils.isNotEmpty(dto.getFileType()), MediaFiles::getFileType, dto.getFileType());
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


    /**
     * 上传普通文件
     * @param companyId 机构id
     * @param dto 文件信息
     * @param localFilePath 本地文件的路径
     */
    @Override
    public MediaFiles uploadFile(Long companyId, UploadFileParamsDto dto, String localFilePath) {
        // 1. 获取文件在minio中的默认存储目录
        String folderPath = getDefaultFolderPath();

        // 2. 设置保存的默认文件名称：md5码+文件后缀名
        // md5码
        String fileMd5 = getFileMd5(localFilePath);
        // 文件后缀名
        String originFilename = dto.getFilename();
        String suffix = originFilename.substring(originFilename.lastIndexOf("."));
        // 组合成文件名
        String filename = fileMd5 + suffix;

        // 3. 得到minio中保存的文件路径
        String objectName = folderPath + filename;

        // 4. 上传文件到minio
        addMediaFilesToMinio(localFilePath, bucketFiles, objectName);

        // 5. 将文件信息添加到数据库表（注意，需要通过代理对象调用，才能保证被事务控制）
        return mediaFileService.addMediaFilesToDB(companyId, dto, objectName, fileMd5, bucketFiles);
    }

    /**
     * 获取文件在minio中的默认存储目录：年/月/日/
     */
    private String getDefaultFolderPath() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date()).replace("-", "/") + "/";
    }

    /**
     * 获取本地文件的MD5值
     */
    private String getFileMd5(String localFilePath) {
        try (FileInputStream is = new FileInputStream(localFilePath)) {
            // 使用 org.apache.commons.codec.digest.DigestUtils 获取MD5值
            return DigestUtils.md5Hex(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据文件的后缀名确定媒体类型
     */
    private String getContentType(String objectName) {
        // 默认content-type为未知二进制流
        String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;

        if (objectName.contains(".")) {
            String suffix = objectName.substring(objectName.lastIndexOf("."));
            // 根据后缀名得到content-type。如果是未知扩展名如.abc，则会返回null
            ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(suffix);
            if (extensionMatch != null) {
                contentType = extensionMatch.getMimeType();
            }
        }
        return contentType;
    }

    /**
     * 通用方法：将本地文件上传到minio
     * @param localFilePath 本地文件路径
     * @param bucketName 桶
     * @param objectName 桶中要保存的文件路径
     */
    private void addMediaFilesToMinio(String localFilePath, String bucketName, String objectName) {
        // 1. 先根据文件的后缀名确定媒体类型
        String contentType = getContentType(objectName);

        // 2. 上传文件到minio
        try {
            minioClient.uploadObject(UploadObjectArgs
                    .builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .filename(localFilePath)
                    .contentType(contentType)
                    .build());
        } catch (Exception e) {
            XueTangException.cast("上传文件到minio出错");
        }
    }

    /**
     * 通用方法：将文件信息添加到数据库表
     */
    @Transactional
    @Override
    public MediaFiles addMediaFilesToDB(Long companyId, UploadFileParamsDto dto,
                                         String objectName, String fileMd5,
                                         String bucketName) {
        MediaFiles mediaFiles = new MediaFiles();
        BeanUtils.copyProperties(dto, mediaFiles);
        mediaFiles.setId(fileMd5);
        mediaFiles.setFileId(fileMd5);
        mediaFiles.setCompanyId(companyId);
        mediaFiles.setBucket(bucketName);
        mediaFiles.setCreateDate(LocalDateTime.now());
        mediaFiles.setStatus("1");
        mediaFiles.setFilePath(objectName);
        mediaFiles.setUrl("/" + bucketName + "/" + objectName);
        mediaFiles.setAuditStatus("002003"); // 审核通过

        int insert = mediaFilesMapper.insert(mediaFiles);
        if (insert <= 0) {
            XueTangException.cast("已经上传过该文件");
        }
        return mediaFiles;
    }


    /**
     * 检查文件是否存在
     */
    @Override
    public RestResponse<Boolean> checkFile(String fileMd5) {
        // 1. 首先查询数据库中是否有该文件记录
        MediaFiles mediaFiles = mediaFilesMapper.selectById(fileMd5);

        // 2. 数据库中不存在，则直接返回false，表示不存在
        if (mediaFiles == null) {
            return RestResponse.success(false);
        }

        // 3. 若数据库中若存在，则根据数据库中的文件信息，继续判断minio中是否存在
        try {
            InputStream inputStream = minioClient.getObject(GetObjectArgs
                    .builder()
                    .bucket(mediaFiles.getBucket())
                    .object(mediaFiles.getFilePath())
                    .build());
            if (inputStream == null) {
                return RestResponse.success(false);
            }
        } catch (Exception e) {
            return RestResponse.success(false);
        }
        return RestResponse.success(true);
    }

    /**
     * 检查分块文件是否存在
     */
    @Override
    public RestResponse<Boolean> checkChunk(String fileMd5, int chunkIndex) {
        // 1. 获取分块文件所在目录
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);

        // 2. 得到分块文件的路径
        String chunkFilePath = chunkFileFolderPath + chunkIndex;
        try {
            // 3. 判断分块文件是否存在
            InputStream inputStream = minioClient.getObject(GetObjectArgs
                    .builder()
                    .bucket(bucketVideo)
                    .object(chunkFilePath)
                    .build());
            // 4. 不存在返回false
            if (inputStream == null) {
                return RestResponse.success(false);
            }
        } catch (Exception e) {
            // 出异常也返回false
            return RestResponse.success(false);
        }
        // 5. 存在则返回true
        return RestResponse.success(true);
    }

    /**
     * 用于获取分块文件所在目录（不包含分块文件的文件名）
     */
    private String getChunkFileFolderPath(String fileMd5) {
        return fileMd5.charAt(0) + "/" + fileMd5.charAt(1) + "/" + fileMd5 + "/chunk/";
    }

    /**
     * 上传分块文件
     */
    @Override
    public RestResponse<Boolean> uploadChunk(String fileMd5, int chunk, String localFilePath) {
        // 1. 获取分块文件路径
        String chunkFilePath = getChunkFileFolderPath(fileMd5) + chunk;

        try {
            // 2. 将分块文件上传到minio
            addMediaFilesToMinio(localFilePath, bucketVideo, chunkFilePath);

            return RestResponse.success(true);
        } catch (Exception e) {
            return RestResponse.validfail("上传分块文件失败", false);
        }
    }


    /**
     * 下载文件并验证MD5
     */
    private boolean downloadAndCheckFile(String bucketName, String objectName, String fileMd5, UploadFileParamsDto uploadFileParamsDto) {
        File tmp = null;
        FileOutputStream os = null;

        try {
            // 1. 创建临时文件
            tmp = File.createTempFile("check", ".tmp");

            // 2. 从minio下载文件
            InputStream is = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());

            // 3. 保存到临时文件
            os = new FileOutputStream(tmp);
            IOUtils.copy(is, os);

            // 4. 比较md5值
            FileInputStream tmpIs = new FileInputStream(tmp);
            boolean res = fileMd5.equals(DigestUtils.md5Hex(tmpIs));

            // 5. 保存文件的大小
            uploadFileParamsDto.setFileSize(tmp.length());

            // 6. 注意一定要关闭流，否则无法删除临时文件
            tmpIs.close();
            os.close();
            is.close();

            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 7. 删除临时文件
            tmp.delete();
        }
    }


    /**
     * 根据MD5和文件扩展名，生成文件路径，例如：/2/f/2f6451sdg/2f6451sdg.mp4
     * @param fileMd5 文件MD5
     * @param extension 文件扩展名
     */
    private String getFilePathByMd5(String fileMd5, String extension) {
        return fileMd5.charAt(0) + "/" + fileMd5.charAt(1) + "/" + fileMd5 + "/" + fileMd5 + extension;
    }

    /**
     * 合并分块文件
     */
    @Override
    public RestResponse<Boolean> mergeChunks(Long companyId, String fileMd5, int chunkTotal, UploadFileParamsDto uploadFileParamsDto) {
        // 1. 获取分块文件所在的目录
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);

        // 2. 将这些分块文件资源组成一个集合
        List<ComposeSource> sourceList = new ArrayList<>();
        for (int i = 0; i < chunkTotal; i++) {
            sourceList.add(ComposeSource.builder()
                    .bucket(bucketVideo)
                    .object(chunkFileFolderPath + i)
                    .build());
        }

        // 3. 合并文件的路径
        // 文件的后缀扩展名
        String filename = uploadFileParamsDto.getFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        // 合并文件的路径
        String mergeFilePath = getFilePathByMd5(fileMd5, suffix);

        // 4. 调用minio的API进行文件合并
        try {
            minioClient.composeObject(ComposeObjectArgs
                    .builder()
                    .bucket(bucketVideo)
                    .object(mergeFilePath)
                    .sources(sourceList)
                    .build());
        } catch (Exception e) {
            return RestResponse.validfail("文件合并失败", false);
        }

        // 5. 下载文件并验证MD5
        boolean b = downloadAndCheckFile(bucketVideo, mergeFilePath, fileMd5, uploadFileParamsDto);
        if (!b) return RestResponse.validfail("文件合并失败", false);

        // 6. 保存文件信息到数据库
        addMediaFilesToDB(companyId, uploadFileParamsDto, mergeFilePath, fileMd5, bucketVideo);

        // 7. 清除minio中的分块文件
        clearChunkFiles(chunkFileFolderPath, chunkTotal);

        return RestResponse.success(true);
    }

    /**
     * 清除minio中的分块文件
     * @param chunkFileFolderPath 分块文件所在的目录
     * @param chunkTotal 分块文件的总数
     */
    private void clearChunkFiles(String chunkFileFolderPath, int chunkTotal) {
        // 1. 将所有的删除分块文件任务添加到一个集合
        List<DeleteObject> deleteObjects = new ArrayList<>();
        for (int i = 0; i < chunkTotal; i++) {
            deleteObjects.add(new DeleteObject(chunkFileFolderPath + i));
        }

        // 2. 进行批量删除
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(RemoveObjectsArgs
                .builder()
                .bucket(bucketVideo)
                .objects(deleteObjects)
                .build());

        // 3. 查看是否有错误信息
        results.forEach(r -> {
            DeleteError deleteError = null;
            try {
                deleteError = r.get();
            } catch (Exception e) {
                log.error("清除分块文件失败, objectName:{}", deleteError.objectName(), e);
            }
        });
    }
}
