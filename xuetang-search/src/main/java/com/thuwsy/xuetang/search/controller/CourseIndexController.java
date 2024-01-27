package com.thuwsy.xuetang.search.controller;

import com.thuwsy.xuetang.base.exception.XueTangException;
import com.thuwsy.xuetang.search.po.CourseIndex;
import com.thuwsy.xuetang.search.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: CourseIndexController
 * Package: com.thuwsy.xuetang.search.controller
 * Description: 课程索引接口
 *
 * @Author THU_wsy
 * @Create 2024/1/24 22:32
 * @Version 1.0
 */
@RestController
@RequestMapping("/search/index")
public class CourseIndexController {

    @Value("${elasticsearch.course.index}")
    private String courseIndexStore;

    @Autowired
    private IndexService indexService;

    /**
     * 添加课程文档
     * @param courseIndex 文档对象
     */
    @PostMapping("/course")
    public Boolean add(@RequestBody CourseIndex courseIndex) {
        Long id = courseIndex.getId();
        if (id == null) {
            XueTangException.cast("课程id为空");
        }

        Boolean result = indexService.addCourseIndex(courseIndexStore, String.valueOf(id), courseIndex);
        if (!result) {
            XueTangException.cast("添加课程索引失败");
        }
        return true;
    }
}
