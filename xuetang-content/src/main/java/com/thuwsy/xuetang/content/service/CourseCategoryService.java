package com.thuwsy.xuetang.content.service;

import com.thuwsy.xuetang.content.po.CourseCategory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 吴晟宇
* @description 针对表【course_category(课程分类)】的数据库操作Service
* @createDate 2024-01-12 14:44:10
*/
public interface CourseCategoryService extends IService<CourseCategory> {
    /**
     * 课程分类树形结构查询
     * @param rootId 树根节点ID
     * @return
     */
    List<CourseCategory> queryTreeNodes(String rootId);
}
