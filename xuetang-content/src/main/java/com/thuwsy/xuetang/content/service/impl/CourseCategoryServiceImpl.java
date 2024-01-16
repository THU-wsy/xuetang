package com.thuwsy.xuetang.content.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thuwsy.xuetang.content.po.CourseCategory;
import com.thuwsy.xuetang.content.service.CourseCategoryService;
import com.thuwsy.xuetang.content.mapper.CourseCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author 吴晟宇
* @description 针对表【course_category(课程分类)】的数据库操作Service实现
* @createDate 2024-01-12 14:44:10
*/
@Service
public class CourseCategoryServiceImpl extends ServiceImpl<CourseCategoryMapper, CourseCategory>
    implements CourseCategoryService {
    @Autowired
    private CourseCategoryMapper courseCategoryMapper;

    @Override
    public List<CourseCategory> queryTreeNodes(String rootId) {
        // 1. 查询出所有分类
        List<CourseCategory> all = courseCategoryMapper.selectList(null);

        // 2. 找到所有一级分类
        List<CourseCategory> list = all.stream().filter(entity ->
            // 父节点id为rootId的即为一级节点
            entity.getParentid().equals(rootId)
        ).map(entity -> {
            // 获取当前分类的所有子分类
            entity.setChildrenTreeNodes(getChildren(entity, all));
            return entity;
        }).sorted((entity1, entity2) -> {
            // 进行排序
            int a = entity1.getOrderby() == null ? 0 : entity1.getOrderby();
            int b = entity2.getOrderby() == null ? 0 : entity2.getOrderby();
            return a - b;
        }).collect(Collectors.toList());

        return list;
    }

    /**
     * 获取当前分类的所有子分类
     * @param current 当前分类
     * @param all 所有分类列表
     * @return
     */
    private List<CourseCategory> getChildren(CourseCategory current, List<CourseCategory> all) {
        List<CourseCategory> list = all.stream().filter(entity ->
            // 父节点id等于current的id的节点即为current的子节点
            entity.getParentid().equals(current.getId())
        ).map(entity -> {
            // 递归获取当前分类的所有子分类
            entity.setChildrenTreeNodes(getChildren(entity, all));
            return entity;
        }).sorted((entity1, entity2) -> {
            // 进行排序
            int a = entity1.getOrderby() == null ? 0 : entity1.getOrderby();
            int b = entity2.getOrderby() == null ? 0 : entity2.getOrderby();
            return a - b;
        }).collect(Collectors.toList());

        return list.size() == 0 ? null : list;
    }
}




