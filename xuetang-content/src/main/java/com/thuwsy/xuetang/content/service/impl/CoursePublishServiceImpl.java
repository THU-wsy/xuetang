package com.thuwsy.xuetang.content.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thuwsy.xuetang.base.exception.XueTangException;
import com.thuwsy.xuetang.content.dto.CourseBaseInfoDto;
import com.thuwsy.xuetang.content.dto.CoursePreviewDto;
import com.thuwsy.xuetang.content.dto.TeachplanDto;
import com.thuwsy.xuetang.content.mapper.CourseBaseMapper;
import com.thuwsy.xuetang.content.mapper.CourseMarketMapper;
import com.thuwsy.xuetang.content.mapper.CoursePublishPreMapper;
import com.thuwsy.xuetang.content.po.CourseBase;
import com.thuwsy.xuetang.content.po.CourseMarket;
import com.thuwsy.xuetang.content.po.CoursePublish;
import com.thuwsy.xuetang.content.po.CoursePublishPre;
import com.thuwsy.xuetang.content.service.CourseBaseService;
import com.thuwsy.xuetang.content.service.CoursePublishService;
import com.thuwsy.xuetang.content.mapper.CoursePublishMapper;
import com.thuwsy.xuetang.content.service.TeachplanService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 吴晟宇
* @description 针对表【course_publish(课程发布)】的数据库操作Service实现
* @createDate 2024-01-12 14:44:10
*/
@Service
public class CoursePublishServiceImpl implements CoursePublishService {

    @Autowired
    private CourseBaseService courseBaseService;

    @Autowired
    private TeachplanService teachplanService;

    @Autowired
    private CourseBaseMapper courseBaseMapper;

    @Autowired
    private CourseMarketMapper courseMarketMapper;

    @Autowired
    private CoursePublishPreMapper coursePublishPreMapper;

    @Autowired
    private CoursePublishMapper coursePublishMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MinioClient minioClient;

    /**
     * 获取预览课程的信息
     * @param courseId 课程id
     * @return CoursePreviewDto
     */
    @Override
    public CoursePreviewDto getCoursePreviewInfo(Long courseId) {
        // 1. 获取课程基本信息、课程营销信息
        CourseBaseInfoDto courseBase = courseBaseService.getCourseBaseById(courseId);

        // 2. 获取课程计划信息
        List<TeachplanDto> teachplans = teachplanService.findTeachplanTree(courseId);

        // 3. ========获取师资信息

        CoursePreviewDto coursePreviewDto = new CoursePreviewDto();
        coursePreviewDto.setCourseBase(courseBase);
        coursePreviewDto.setTeachplans(teachplans);
        return coursePreviewDto;
    }

    /**
     * 提交课程进行审核
     * @param companyId 机构id
     * @param courseId 课程id
     */
    @Transactional
    @Override
    public void commitAudit(Long companyId, Long courseId) {
        // 1. 查询课程的相关信息
        // 查询课程基本信息、课程营销信息
        CourseBaseInfoDto courseBaseInfo = courseBaseService.getCourseBaseById(courseId);
        // 查询课程营销信息（用于设置json格式的market字段）
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        // 查询课程计划
        List<TeachplanDto> teachplanTree = teachplanService.findTeachplanTree(courseId);

        // 2. 约束校验
        // 当前课程审核状态为已提交时，不可再次提交
        if ("202003".equals(courseBaseInfo.getAuditStatus())) {
            XueTangException.cast("该课程正在审核中，审核完成后才可以再次提交");
        }
        // 只允许提交本机构的课程
        if (!companyId.equals(courseBaseInfo.getCompanyId())) {
            XueTangException.cast("不允许提交其他机构的课程");
        }
        // 课程没有上传图片，不允许提交
        if (StringUtils.isEmpty(courseBaseInfo.getPic())) {
            XueTangException.cast("提交失败，请上传课程图片");
        }
        // 没有添加课程计划，不允许提交
        if (teachplanTree == null || teachplanTree.isEmpty()) {
            XueTangException.cast("没有添加课程计划，不允许提交");
        }

        // 3. 封装课程预发布记录

        CoursePublishPre coursePublishPre = new CoursePublishPre();
        BeanUtils.copyProperties(courseBaseInfo, coursePublishPre);
        coursePublishPre.setMarket(JSON.toJSONString(courseMarket));
        coursePublishPre.setTeachplan(JSON.toJSONString(teachplanTree));
        coursePublishPre.setCompanyId(companyId);
        coursePublishPre.setCreateDate(LocalDateTime.now());
//        coursePublishPre.setStatus("202003"); // 设置审核状态为已提交
        // 我们为了简便，直接省略审核过程，将课程的审核状态设置为审核通过
        coursePublishPre.setStatus("202004");


        // 4. 判断是否已存在该课程的预发布记录。若存在则更新，否则就新增。
        CoursePublishPre tmp = coursePublishPreMapper.selectById(courseId);
        if (tmp == null) {
            coursePublishPreMapper.insert(coursePublishPre);
        } else {
            coursePublishPreMapper.updateById(coursePublishPre);
        }

        // 5. 将课程基本信息表中该课程的审核状态修改为已提交
        CourseBase courseBase = new CourseBase();
        courseBase.setId(courseId);
//        courseBase.setAuditStatus("202003");
        // 我们为了简便，直接省略审核过程，将课程的审核状态设置为审核通过
        courseBase.setAuditStatus("202004");

        courseBaseMapper.updateById(courseBase);
    }

    /**
     * 发布课程
     * @param companyId 机构id
     * @param courseId 课程id
     */
    @Transactional
    @Override
    public void coursePublish(Long companyId, Long courseId) {
        // 1. 约束校验
        // 获取课程预发布表数据
        CoursePublishPre coursePublishPre = coursePublishPreMapper.selectById(courseId);
        // 只有审核通过后，才可发布
        if (coursePublishPre == null || !"202004".equals(coursePublishPre.getStatus())) {
            XueTangException.cast("提交课程等待审核通过后，才可进行发布");
        }
        // 只允许发布本机构的课程
        if (!coursePublishPre.getCompanyId().equals(companyId)) {
            XueTangException.cast("只允许发布本机构的课程");
        }

        // 2. 将预发布表中的记录，保存到课程发布表
        CoursePublish coursePublish = saveCoursePublish(coursePublishPre);

        // 3. 发送课程发布的消息到消息队列
        String json = JSON.toJSONString(coursePublish);
        Message message = new Message(json.getBytes());
        rabbitTemplate.convertAndSend("exchange.xuetang.publish", "", message);

        // 4. 删除课程预发布表对应记录
        coursePublishPreMapper.deleteById(courseId);
    }


    /**
     * 保存课程发布信息
     * @param coursePublishPre 课程预发布表中的记录
     */
    private CoursePublish saveCoursePublish(CoursePublishPre coursePublishPre) {
        // 1. 封装数据
        CoursePublish coursePublish = new CoursePublish();
        BeanUtils.copyProperties(coursePublishPre, coursePublish);
        // 设置状态为已发布
        coursePublish.setStatus("203002");

        // 2. 对课程发布表进行新增或修改
        CoursePublish tmp = coursePublishMapper.selectById(coursePublish.getId());
        if (tmp == null) {
            coursePublishMapper.insert(coursePublish);
        } else  {
            coursePublishMapper.updateById(coursePublish);
        }

        // 3. 更新课程基本信息表的发布状态
        CourseBase courseBase = new CourseBase();
        courseBase.setId(coursePublish.getId());
        courseBase.setStatus("203002");
        courseBaseMapper.updateById(courseBase);

        return coursePublish;
    }


    /**
     * 将发布的课程生成静态页面，并上传至minio
     * @param courseId 课程id
     */
    @Override
    public void coursePublishToMinio(Long courseId) {
        // 1. 创建临时文件
        File htmlFile = null;
        try {
            htmlFile = File.createTempFile("course", ".html");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 2. 生成静态页面，并写入该临时文件
        generateStaticHtml(htmlFile, courseId);

        // 3. 将临时文件上传到minio，路径为 mediafiles/course/课程id.html
        uploadCourseToMinio(htmlFile, courseId);

        // 4. 删除临时文件
        htmlFile.delete();
    }

    /**
     * 生成静态页面，并写入该临时文件
     */
    private void generateStaticHtml(File htmlFile, Long courseId) {
        // 1. 准备课程预览的数据
        CoursePreviewDto coursePreviewDto = getCoursePreviewInfo(courseId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("model", coursePreviewDto);

        // 2. 配置freemarker
        Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);

        try {
            // 3. 加载模板
            String classpath = this.getClass().getResource("/").getPath();
            configuration.setDirectoryForTemplateLoading(new File(classpath + "/templates/"));
            configuration.setDefaultEncoding("utf-8");
            Template template = configuration.getTemplate("course_template.ftl");

            // 4. 静态化：第一个参数是模板，第二个参数是数据模型
            String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);

            // 5. 将静态化页面输出到文件中
            FileOutputStream os = new FileOutputStream(htmlFile);
            ByteArrayInputStream is = new ByteArrayInputStream(content.getBytes());
            IOUtils.copy(is, os);

            // 6. 关闭流
            is.close();
            os.close();

        } catch (Exception e) {
            XueTangException.cast("课程预览页面静态化出现错误");
        }
    }

    /**
     * 上传静态页面到minio
     */
    private void uploadCourseToMinio(File htmlFile, Long courseId) {
        // 路径为 mediafiles/course/课程id.html
        String bucketName = "mediafiles";
        String objectName = "course/" + courseId + ".html";
        String contentType = "text/html";

        // 上传到minio
        try {
            minioClient.uploadObject(UploadObjectArgs
                    .builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .filename(htmlFile.getAbsolutePath())
                    .contentType(contentType)
                    .build());
        } catch (Exception e) {
            XueTangException.cast("上传文件到minio出错");
        }
    }
}




