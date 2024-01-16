package com.thuwsy.xuetang.content.controller;

import com.thuwsy.xuetang.content.po.CourseCategory;
import com.thuwsy.xuetang.content.service.CourseCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ClassName: CourseCategoryController
 * Package: com.thuwsy.xuetang.content.controller
 * Description: CourseCategoryController
 *
 * @Author THU_wsy
 * @Create 2024/1/16 22:47
 * @Version 1.0
 */
@RestController
@RequestMapping("/content/course-category")
public class CourseCategoryController {
    @Autowired
    private CourseCategoryService courseCategoryService;

    /**
     * 查询课程多级分类列表：查出所有分类以及子分类，以树形结构组装起来
     */
    @GetMapping("/tree-nodes")
    public List<CourseCategory> queryTreeNodes() {
        // 参数为根节点id，数据库中根节点id为1（String类型）
        return courseCategoryService.queryTreeNodes("1");
    }
}
