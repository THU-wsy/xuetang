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
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.errors.*;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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

    @Override
    public MediaFiles uploadFile(Long companyId, UploadFileParamsDto dto, byte[] bytes) {
        // 1. 文件的默认存储目录路径
        String path = getDefaultFolderPath();

        // 2. 文件的默认名称：md5码+文件后缀名
        String fileMD5 = DigestUtils.md5DigestAsHex(bytes);
        String originFilename = dto.getFilename();
        String suffix = originFilename.substring(originFilename.lastIndexOf("."));
        String filename = fileMD5 + suffix;

        // 3. 文件路径
        String objectName = path + filename;

        // 4. 上传到MinIO
        addMediaFilesToMinio(bytes, bucketFiles, objectName);

        // 5. 保存到数据库（注意，需要通过代理对象调用，才能保证被事务控制）
        return mediaFileService.addMediaFilesToDB(companyId, dto, objectName, fileMD5, bucketFiles, bytes.length);
    }

    /**
     * 获取文件的默认存储目录路径：年/月/日/
     */
    private String getDefaultFolderPath() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date()).replace("-", "/") + "/";
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
     * 上传文件到MinIO
     * @param bytes 文件字节数组
     * @param bucketName 桶名称
     * @param objectName 文件路径
     */
    private void addMediaFilesToMinio(byte[] bytes, String bucketName, String objectName) {
        // 1. 先根据文件的后缀名确定媒体类型
        String contentType = getContentType(objectName);

        // 2. 上传文件到minio
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(is, is.available(), -1)
                    .contentType(contentType)
                    .build());
        } catch (Exception e) {
            XueTangException.cast("上传文件出错");
        }
    }

    /**
     * 将文件信息添加到数据库表
     */
    @Transactional
    @Override
    public MediaFiles addMediaFilesToDB(Long companyId, UploadFileParamsDto dto,
                                         String objectName, String fileMD5,
                                         String bucketName, long fileSize) {
        MediaFiles mediaFiles = new MediaFiles();
        BeanUtils.copyProperties(dto, mediaFiles);
        mediaFiles.setId(fileMD5);
        mediaFiles.setFileId(fileMD5);
        mediaFiles.setCompanyId(companyId);
        mediaFiles.setBucket(bucketName);
        mediaFiles.setCreateDate(LocalDateTime.now());
        mediaFiles.setStatus("1");
        mediaFiles.setFilePath(objectName);
        mediaFiles.setUrl("/" + bucketName + "/" + objectName);
        mediaFiles.setAuditStatus("002003"); // 审核通过
        mediaFiles.setFileSize(fileSize);

        int insert = mediaFilesMapper.insert(mediaFiles);
        if (insert <= 0) {
            XueTangException.cast("已经上传过该文件");
        }
        return mediaFiles;
    }


    @Override
    public RestResponse<Boolean> checkFile(String fileMd5) {
        MediaFiles mediaFiles = mediaFilesMapper.selectById(fileMd5);
        // 1. 数据库中不存在，则直接返回false，表示不存在
        if (mediaFiles == null) {
            return RestResponse.success(false);
        }

        // 2. 若数据库中存在，根据数据库中的文件信息，则继续判断minio中是否存在
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

    @Override
    public RestResponse<Boolean> checkChunk(String fileMd5, int chunkIndex) {
        // 1. 获取分块目录
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
        return RestResponse.success();
    }

    /**
     * 用于获取分块文件所在目录（不包含分块文件的文件名）
     */
    private String getChunkFileFolderPath(String fileMd5) {
        return fileMd5.charAt(0) + "/" + fileMd5.charAt(1) + "/" + fileMd5 + "/chunk/";
    }

    @Override
    public RestResponse<Boolean> uploadChunk(String fileMd5, int chunk, byte[] bytes) {
        // 获取分块文件路径
        String chunkFilePath = getChunkFileFolderPath(fileMd5) + chunk;
        try {
            addMediaFilesToMinio(bytes, bucketVideo, chunkFilePath);
            return RestResponse.success(true);
        } catch (Exception e) {
            XueTangException.cast("上传分块文件失败！");
        }
        return RestResponse.validfail("上传文件失败", false);
    }


    /**
     * 下载分块文件
     * @param fileMd5 文件的MD5
     * @param chunkTotal 总块数
     * @return 分块文件数组
     */
    private File[] checkChunkStatus(String fileMd5, int chunkTotal) {
        // 作为结果返回
        File[] files = new File[chunkTotal];

        // 获取分块文件所在的目录
        String chunkFileFolder = getChunkFileFolderPath(fileMd5);
        for (int i = 0; i < chunkTotal; i++) {
            // 获取分块文件路径
            String chunkFilePath = chunkFileFolder + i;
            File chunkFile = null;
            try {
                // 创建临时的分块文件
                chunkFile = File.createTempFile("chunk" + i, null);
            } catch (Exception e) {
                XueTangException.cast("创建临时分块文件出错：" + e.getMessage());
            }
            // 下载分块文件
            chunkFile = downloadFileFromMinio(chunkFile, bucketVideo, chunkFilePath);
            // 组成结果
            files[i] = chunkFile;
        }
        return files;
    }

    /**
     * 从Minio中下载文件
     * @param file 目标文件
     * @param bucketName 桶
     * @param objectName 桶内文件路径
     * @return
     */
    private File downloadFileFromMinio(File file, String bucketName, String objectName) {
        try (FileOutputStream os = new FileOutputStream(file);
             InputStream is = minioClient.getObject(GetObjectArgs
                     .builder()
                     .bucket(bucketName)
                     .object(objectName)
                     .build())) {
            IOUtils.copy(is, os);
            return file;
        } catch (Exception e) {
            XueTangException.cast("下载分块文件失败");
        }
        return null;
    }


    @Override
    public RestResponse<Boolean> mergeChunks(Long companyId, String fileMd5, int chunkTotal, UploadFileParamsDto uploadFileParamsDto) {
        // 下载分块文件
        File[] chunkFiles = checkChunkStatus(fileMd5, chunkTotal);
        // 获取源文件名
        String fileName = uploadFileParamsDto.getFilename();
        // 获取源文件扩展名
        String extension = fileName.substring(fileName.lastIndexOf("."));
        // 创建出临时文件，准备合并
        File mergeFile = null;
        try {
            mergeFile = File.createTempFile(fileName, extension);
        } catch (IOException e) {
            XueTangException.cast("创建合并临时文件出错");
        }
        try {
            // 缓冲区
            byte[] buffer = new byte[1024];
            // 写入流，向临时文件写入
            try (RandomAccessFile raf_write = new RandomAccessFile(mergeFile, "rw")) {
                // 遍历分块文件数组
                for (File chunkFile : chunkFiles) {
                    // 读取流，读分块文件
                    try (RandomAccessFile raf_read = new RandomAccessFile(chunkFile, "r")) {
                        int len;
                        while ((len = raf_read.read(buffer)) != -1) {
                            raf_write.write(buffer, 0, len);
                        }
                    }
                }
            } catch (Exception e) {
                XueTangException.cast("合并文件过程中出错");
            }
            uploadFileParamsDto.setFileSize(mergeFile.length());
            // 对文件进行校验，通过MD5值比较
            try (FileInputStream mergeInputStream = new FileInputStream(mergeFile)) {
                String mergeMd5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(mergeInputStream);
                if (!fileMd5.equals(mergeMd5)) {
                    XueTangException.cast("合并文件校验失败");
                }
            } catch (Exception e) {
                XueTangException.cast("合并文件校验异常");
            }
            String mergeFilePath = getFilePathByMd5(fileMd5, extension);
            // 将本地合并好的文件，上传到minio中，这里重载了一个方法
            addMediaFilesToMinio(mergeFile.getAbsolutePath(), bucketVideo, mergeFilePath);
            // 将文件信息写入数据库
            MediaFiles mediaFiles = mediaFileService.addMediaFilesToDB(companyId, uploadFileParamsDto,
                    mergeFilePath, fileMd5, bucketVideo, uploadFileParamsDto.getFileSize());
            if (mediaFiles == null) {
                XueTangException.cast("媒资文件入库出错");
            }
            return RestResponse.success();
        } finally {
            for (File chunkFile : chunkFiles) {
                try {
                    chunkFile.delete();
                } catch (Exception e) {
                    XueTangException.cast("临时分块文件删除错误");
                }
            }
            try {
                mergeFile.delete();
            } catch (Exception e) {
                XueTangException.cast("临时合并文件删除错误");
            }
        }
    }

    /**
     * 将本地文件上传到minio
     * @param filePath      本地文件路径
     * @param bucketName        桶
     * @param objectName    对象名称
     */
    private void addMediaFilesToMinio(String filePath, String bucketName, String objectName) {
        String contentType = getContentType(objectName);
        try {
            minioClient.uploadObject(UploadObjectArgs
                    .builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .filename(filePath)
                    .contentType(contentType)
                    .build());
        } catch (Exception e) {
            XueTangException.cast("上传到文件系统出错");
        }
    }

    /**
     * 根据MD5和文件扩展名，生成文件路径，例 /2/f/2f6451sdg/2f6451sdg.mp4
     * @param fileMd5       文件MD5
     * @param extension     文件扩展名
     * @return
     */
    private String getFilePathByMd5(String fileMd5, String extension) {
        return fileMd5.charAt(0) + "/" + fileMd5.charAt(1) + "/" + fileMd5 + "/" + fileMd5 + extension;
    }
}
